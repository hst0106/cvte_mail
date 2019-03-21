package com.cvte.demo.common;

/**
 * @Author: huangShunTong
 * @Date: 2019/3/21 14:15
 * @description
 */
public enum ResponseCode {
	/*
	* 成功
	* */
	SUCCESS(0,"SUCCESS"),

    /*
    * 失败
    * */
	ERROR(1,"ERROR"),

    /*
    * 不合法
    * */
	ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
	
	private final int code;
	private final String desc;
    
    ResponseCode(int code, String desc) {
    	this.code = code;
    	this.desc = desc;
	}
    
    public int getCode() {
    	return code;
    }
    
    public String getDesc() {
    	return desc;
    }
}
