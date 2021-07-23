package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//Classe para personalizar nosso json de requisição inválida
@RestControllerAdvice
public class ErroDeValidacaoHandler {

	//Classe responsável por montar nossa mensagem
	@Autowired
	private MessageSource messageSource;
	
	//Obrigatório colocar para ele saber que só deve aparecer essa personalização de erro quando for BAD_REQUEST
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class) //Classe Spring que obtem os erros de BADREQUEST
	public List<ErroDeFormDTO> handle(MethodArgumentNotValidException excpetion) {
		List<ErroDeFormDTO> errosForm = new ArrayList<ErroDeFormDTO>();
		List<FieldError> fieldErros = excpetion.getBindingResult().getFieldErrors();
		
		fieldErros.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormDTO erro = new ErroDeFormDTO(e.getField(), mensagem);
			errosForm.add(erro);
		});
		
		return errosForm;
	}
}
