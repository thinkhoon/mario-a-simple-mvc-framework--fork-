package com.junicorn.mario.servlet.wrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest增强
 * @author biezhi
 *
 */
public class Request {

	private HttpServletRequest raw;
	
	public Request(HttpServletRequest httpServletRequest) {
		this.raw = httpServletRequest;
	}

	public HttpServletRequest getRaw() {
		return raw;
	}
	
	public void attr(String name, Object value){
		raw.setAttribute(name, value);
	}
	
	@SuppressWarnings("unchecked")
	//此注解告诉编译器无视未检查过的强制转换所带来的警告
	public <T> T attr(String name){
		Object value = raw.getAttribute(name);
		if(null != value){
			return (T) value;
		}
		return null;
	}
	
	public String query(String name){
		return raw.getParameter(name);
	}
	
	public Integer queryAsInt(String name){
		String val = query(name);
		if(null != val && !val.equals("")){
			return Integer.valueOf(val);
		}
		return null;
	}
	
	public String cookie(String name){
		Cookie[] cookies = raw.getCookies();
		if(null != cookies && cookies.length > 0){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals(name)){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
