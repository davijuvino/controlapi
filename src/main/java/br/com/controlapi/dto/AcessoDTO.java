package br.com.controlapi.dto;

public class AcessoDTO {

	private String token;

	//TODO implementar retornar o usuario e liberacoes (authorities)
	
	public AcessoDTO(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	
	
}
