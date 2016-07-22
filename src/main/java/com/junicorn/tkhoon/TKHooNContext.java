package com.junicorn.tkhoon;

import javax.servlet.ServletContext;

import com.junicorn.tkhoon.servlet.wrapper.Request;
import com.junicorn.tkhoon.servlet.wrapper.Response;

/**
 * 当前线程上下文环境
 * @author biezhi
 */
public final class TKHooNContext {

	private static final ThreadLocal<TKHooNContext> CONTEXT = new ThreadLocal<TKHooNContext>();
	
	private ServletContext context;
	private Request request;
	private Response response;

	private TKHooNContext() {
	}
	
	public static TKHooNContext me(){
		return CONTEXT.get();
	}
	
    public static void initContext(ServletContext context, Request request, Response response) {
    	TKHooNContext TKHooNContext = new TKHooNContext();
    	TKHooNContext.context = context;
    	TKHooNContext.request = request;
    	TKHooNContext.response = response;
    	CONTEXT.set(TKHooNContext);
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
