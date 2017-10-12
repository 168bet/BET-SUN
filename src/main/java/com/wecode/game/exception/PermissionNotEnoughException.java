package com.wecode.game.exception;

public class PermissionNotEnoughException extends CommonException {
	private String user;
	private String permission;
	private Long id;

	public PermissionNotEnoughException() {
		super();
	}

	public PermissionNotEnoughException(Exception e) {
		super(e);
	}

	public PermissionNotEnoughException(String user, String permission, Long id) {
		super(user + " dont have permission " + permission);
		this.user = user;
		this.permission = permission;
		this.id = id;
	}

	private static final long serialVersionUID = -1L;

	@Override
	public String getErrorMsg() {
		return user + " dont have permission " + permission + " id:" + this.id;
	}

	@Override
	protected int getIndex() {
		return 1;
	}
}
