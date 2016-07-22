package com.junicorn.tkhoon.route;

import java.lang.reflect.Method;

/**
 * 路由
 * @author biezhi
 */
public class Route {

	/**
	 * 路由path
	 */
	private String path;

	/**
	 * 执行路由的方法
	 */
	private Method action;

	/**
	 * 路由所在的控制器
	 */
	private Object controller;

	public Route() {
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Method getAction() {
		return action;
	}

	public void setAction(Method action) {
		this.action = action;
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

}
