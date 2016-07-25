package com.junicorn.mario;

import javax.servlet.ServletContext;

import com.junicorn.mario.servlet.wrapper.Request;
import com.junicorn.mario.servlet.wrapper.Response;

/**
 * 当前线程上下文环境
 * @author biezhi
 */
public final class MarioContext {

	private static final ThreadLocal<MarioContext> CONTEXT = new ThreadLocal<MarioContext>();
	
	private ServletContext context;
	private Request request;
	private Response response;

	private MarioContext() {
	}
	
	public static MarioContext me(){
		return CONTEXT.get();
	}
	
    public static void initContext(ServletContext context, Request request, Response response) {
    	MarioContext marioContext = new MarioContext();
    	marioContext.context = context;
    	marioContext.request = request;
    	marioContext.response = response;
    	CONTEXT.set(marioContext);
    }
    
    public static void remove(){
    	CONTEXT.remove();
    }
	
	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
