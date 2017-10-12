package com.wecode.game.bean;

public class Bill {
	public String uuid;
	public String userId;
	public Double point;
	public Integer type;
	public String info;
	public Integer cardType;
	public String cardNum;
	public String cardArea;
	public String cardCity;
	public String cardSite;
	public Integer state;
	public String username;
	public String name;
	public Long createdTime;
	public Double totalPoint;
	public String phone;
	public String captcha;
	public String password;

	public Bill() {

	}

	public Bill(String uuid) {
		this.uuid = uuid;
	}
}
