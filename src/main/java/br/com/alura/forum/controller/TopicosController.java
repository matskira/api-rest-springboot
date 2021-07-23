package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso) {

		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.converterTopico(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDTO.converterTopico(topicos);
		}

	}

	@PostMapping // O responseEntity é uma classe do Spring que nos permite controlar o resultado
					// da requisição
	@Transactional
	public ResponseEntity<TopicoDTO> cadastrarTopico(@RequestBody @Valid TopicoForm topicoForm,
			UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		/*
		 * Quando cadastramos um novo recurso é comum devolvermos 201 e não apenas o
		 * 200, para isso utilizamos o método create do ResponseEntity, porém ele espera
		 * uma cabeçaçalho e um corpo, contendo o novo endereço com esse recurso criado
		 * e o próprio recurso. Para isso precisamos criar uma uri e utilizar outra
		 * classe do Spring UriComponentsBuilder.
		 */
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}

	// Traz o detalhe de um tópico específico
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);

		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));
		}

		return ResponseEntity.notFound().build();
	}

	// Atualiza o tópico específico
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> autalizaTopico(@PathVariable Long id,
			@RequestBody @Valid AtualizacaoTopicoForm topicoForm) {

		Optional<Topico> optionalTopico = topicoRepository.findById(id);

		if (optionalTopico.isPresent()) {
			Topico topico = topicoForm.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		return ResponseEntity.notFound().build();
	}

	// Exclui um tópico específico
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> removeTopico(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
