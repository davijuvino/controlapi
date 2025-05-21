package br.com.controlapi.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoUsuario {

	ATIVO("A", "Ativo"),
	INATIVO("I", "Inativo"),
	PENDENTE("P", "Pendente");

	private final String codigo;
	private final String descricao;

	SituacaoUsuario(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	@JsonValue
	public String getCodigo() {
		return codigo;
	}

	@JsonValue
	public String getDescricao() {
		return descricao;
	}

	@JsonCreator
	public static SituacaoUsuario valorDo(String codigo) {
		return switch (codigo) {
			case "A" -> ATIVO;
			case "I" -> INATIVO;
			case "P" -> PENDENTE;
			default -> null;
		};
	}
}