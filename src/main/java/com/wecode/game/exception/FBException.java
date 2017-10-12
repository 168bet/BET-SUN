package com.wecode.game.exception;

public abstract class FBException extends Exception {

	private static final long serialVersionUID = 7466342127489979154L;
	protected static final int CURRENT_BASE_ERROR_CODE = 950;

	public FBException() {
	}

	public FBException(String msg) {
	}

	// 保留原始异常信息
	public FBException(Exception e) {
		super(e);
	}

	public final int getErrorCode() {
		return getBaseErrorCode() + getIndex();
	}

	public abstract String getErrorMsg();

	protected abstract int getBaseErrorCode();

	protected abstract int getIndex();
}
