package com.gmart.api.core.entities.enums;

public enum PostType {
	VIDEO("PP"), TEXT(""), IMAGE(""), LINK("CP");

	private String type;

	public String getType() {
		return this.type;
	}

	private PostType(String type) {
		this.type = type;
	}
}
