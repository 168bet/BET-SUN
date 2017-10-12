package com.wecode.game.util;

public class HTTP_RESPONCES_INFO 
{

	public static final int SUCCESS = 200;
	public static final int UNKOWN = 501;
	public static final String UNKNOWN_MSG = "未知错误";
	/**************通用*****************/
	public static final int COMMON_NUM = 600;
	public static final int PERMISSION_NOT_ENOUGH = COMMON_NUM + 1;	
	public static final String PERMISSION_NOT_ENOUGH_MSG = "permission not enough";
	public static final int RESUALT_PARSE_ERROR = COMMON_NUM + 4;
	public static final String RESUALT_PARSE_ERROR_MSG = "resualt parse error";
	
	
	
	/**************用户模块*****************/
	public static final int USER_NUM = 650;//用户模块起始的返回码
	
	public static final int USERNAME_NOT_EXIST = USER_NUM + 1;
	public static final String USERNAME_NOT_EXIST_MSG = "username is not exist";
	
	public static final int PASSWORD_INCORRECT = USER_NUM + 2;
	public static final String PASSWORD_INCORRECT_MSG = "password incorrect";
	
	public static final int USER_NOT_EXIST = USER_NUM + 3;
	public static final String USER_NOT_EXIST_MSG = "用户不存在";
	
	public static final int USER_IS_LOCKED = USER_NUM + 4;
	public static final String USER_IS_LOCKED_MSG = "该用户被锁定，请咨询管理员解锁";
	
	public static final int USER_IS_STOPPED = USER_NUM + 5;
	public static final String USER_IS_STOPPED_MSG = "该用户被停用";
	
	public static final int PWD_ERROR_THAN_FIVE = USER_NUM + 6;
	public static final String PWD_ERROR_THAN_FIVE_MSG = "输入5次密码，该账号已被锁定，请找管理员解锁";

	public static final int USERNAME_IS_EXIST = USER_NUM + 7;
	public static final String USERNAME_IS_EXIST_MSG = "用户名已存在";

	
	/*************代理********************/
	public static final int AGENT_NUM = 700;//代理模块起始的返回码
	public static final int NOT_FIND_AGENT = AGENT_NUM + 1;
	public static final String NOT_FIND_AGENT_MSG = "can't find the agent by uuid";
	
	
	/*************会员*********************/
	public static final int MEMBER_NUM = 750;//代理模块起始的返回码
	public static final int MEMBER_NOT_EXIST = MEMBER_NUM + 1;
	public static final String MEMBER_NOT_EXIST_MSG = "会员不存在";
	public static final int NAME_IN_CORRECT = MEMBER_NUM + 2;
	public static final String NAME_IN_CORRECT_MSG = "真实姓名不正确";
	public static final int MONEY_PASSWORD_INCORRECT = MEMBER_NUM + 3;
	public static final String MONEY_PASSWORD_INCORRECT_MSG = "取款密码错误";

	/**************管理员******************/
	public static final int MANAGER_NUM = 800;
	public static final int ROLE_NOT_RIGHT = MANAGER_NUM + 1;
	public static final String ROLE_NOT_RIGHT_MSG = "角色不正确";
	/*************验证码*****************/
	public static final int CAPTCHA_ERROR_NUM = 900;//验证码错误起始的返回码
	public static final int CAPTCHA_INCRECT = CAPTCHA_ERROR_NUM + 1;
	public static final String CAPTCHA_INCRECT_MSG = "验证码错误";

}
