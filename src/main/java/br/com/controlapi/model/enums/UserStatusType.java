package br.com.controlapi.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatusType {

	ATIVO("A", "Ativo"),
	INATIVO("I", "Inativo"),
	PENDENTE("P", "Pendente");

	private final String code;
	private final String description;

	UserStatusType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static UserStatusType ofValue(String code) {
		return switch (code) {
			case "A" -> ATIVO;
			case "I" -> INATIVO;
			case "P" -> PENDENTE;
			default -> null;
		};
	}
}