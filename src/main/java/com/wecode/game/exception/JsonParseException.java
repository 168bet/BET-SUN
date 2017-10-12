package com.wecode.game.exception;

public class JsonParseException extends CommonException {

	private static final long serialVersionUID = 4579052398106315280L;

	private String json;

	public JsonParseException(Exception e, String json) {
		super(e);
		this.json = json;
	}

	@Override
	public String getErrorMsg() {
		return "Json Parse Error, json body: " + this.json;
	}

	@Override
	protected int getIndex() {
		return 5;
	}

}
