package br.com.alura.forum.config.validacao;

public class ErroDeFormDTO {

	private String erro;
	private String campo;

	public ErroDeFormDTO(String erro, String campo) {
		this.erro = erro;
		this.campo = campo;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

}