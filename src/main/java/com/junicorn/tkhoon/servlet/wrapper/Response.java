package com.junicorn.tkhoon.servlet.wrapper;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.junicorn.tkhoon.TKHooN;
import com.junicorn.tkhoon.render.Render;

/**
 * HttpServletResponse增强
 * @author biezhi
 *
 */
public class Response {

	private HttpServletResponse raw;
	
	private Render render = null;
	
	public Response(HttpServletResponse httpServletResponse) {
		this.raw = httpServletResponse;
		this.raw.setHeader("Framework", "TKHooN");
		this.render = TKHooN.me().getRender();
	}
	
	public void text(String text) {
		raw.setContentType("text/plan;charset=UTF-8");
		this.print(text);
	}

	public void html(String html) {
		raw.setContentType("text/html;charset=UTF-8");
		this.print(html);
	}
	
	private void print(String str){
		try {
			OutputStream outputStream = raw.getOutputStream();
			outputStream.write(str.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cookie(String name, String value){
		Cookie cookie = new Cookie(name, value);
		raw.addCookie(cookie);
	}
	
	public HttpServletResponse getRaw() {
		return raw;
	}
	
	public void render(String view) {
		render.render(view, null);
	}

	public void redirect(String location) {
		try {
			raw.sendRedirect(location);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}