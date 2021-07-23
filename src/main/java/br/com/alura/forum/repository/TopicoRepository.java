package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	//Quando você coloca o '_' o Spring entende que o Curso é um relacionamento e que na tabela de Curso queremo o parâmetro Nome;
	List<Topico> findByCurso_Nome(String nomeCurso);

}
