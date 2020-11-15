package com.gmart.api.core.entities.enums;

public enum PictureTypeEnum {

	PROFILE_PICTURE("PP"),
	COVER_PICTURE("CP");
	private String type;
	
	public String getType() {
		return this.type;
	}
	
	private PictureTypeEnum(String type) {
	this.type = type;	
	}
}
