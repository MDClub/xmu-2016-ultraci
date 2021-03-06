package com.uitraci.hotel.dto;

import java.io.Serializable;

/**
 * 对”登录“请求的响应
 * 
 * @author LuoXin
 *
 */
public class LoginDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 2019169072862004103L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
