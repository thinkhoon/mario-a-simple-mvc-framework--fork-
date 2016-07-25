package com.junicorn.mario;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.junicorn.mario.route.Route;
import com.junicorn.mario.route.RouteMatcher;
import com.junicorn.mario.route.Routers;
import com.junicorn.mario.servlet.wrapper.Request;
import com.junicorn.mario.servlet.wrapper.Response;
import com.junicorn.mario.util.PathUtil;
import com.junicorn.mario.util.ReflectUtil;

/**
 * Mario MVC核心处理器
 * @author biezhi
 *
 */
public class MarioFilter implements Filter {
	
	private static final Logger LOGGER = Logger.getLogger(MarioFilter.class.getName());
	
	private RouteMatcher routeMatcher = new RouteMatcher(new ArrayList<Route>());
	
	private ServletContext servletContext;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Mario mario = Mario.me();
		if(!mario.isInit()){
			
			String className = filterConfig.getInitParameter("bootstrap");
			Bootstrap bootstrap = this.getBootstrap(className);
			bootstrap.init(mario);
			
			Routers routers = mario.getRouters();
			if(null != routers){
				routeMatcher.setRoutes(routers.getRoutes());
			}
			servletContext = filterConfig.getServletContext();
			
			mario.setInit(true);
		}
	}
	
	private Bootstrap getBootstrap(String className) {
		if(null != className){
			try {
				Class<?> clazz = Class.forName(className);
				Bootstrap bootstrap = (Bootstrap) clazz.newInstance();
				return bootstrap;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("init bootstrap class error!");
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding(Const.DEFAULT_CHAR_SET);
        response.setCharacterEncoding(Const.DEFAULT_CHAR_SET);
        
        // 请求的uri
        String uri = PathUtil.getRelativePath(request);
        
        LOGGER.info("Request URI：" + uri);
        
        Route route = routeMatcher.findRoute(uri);
        
        // 如果找到
		if (route != null) {
			// 实际执行方法
			handle(request, response, route);
		} else{
			chain.doFilter(request, response);
		}
	}
	
	private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Route route){
		
		// 初始化上下文
		Request request = new Request(httpServletRequest);
		Response response = new Response(httpServletResponse);
		MarioContext.initContext(servletContext, request, response);
		
		Object controller = route.getController();
		// 要执行的路由方法
		Method actionMethod = route.getAction();
		// 执行route方法
		executeMethod(controller, actionMethod, request, response);
	}
	
	/**
	 * 获取方法内的参数 其他的都没有获取到啊
	 */
	private Object[] getArgs(Request request, Response response, Class<?>[] params){
		
		int len = params.length;
		Object[] args = new Object[len];
		
		for(int i=0; i<len; i++){
			Class<?> paramTypeClazz = params[i];
			if(paramTypeClazz.getName().equals(Request.class.getName())){
				args[i] = request;
			}
			if(paramTypeClazz.getName().equals(Response.class.getName())){
				args[i] = response;
			}
		}
		
		return args;
	}
	
	/**
	 * 执行路由方法
	 */
	private Object executeMethod(Object object, Method method, Request request, Response response){
		int len = method.getParameterTypes().length;
		method.setAccessible(true);
		if(len > 0){
			Object[] args = getArgs(request, response, method.getParameterTypes());
			return ReflectUtil.invokeMehod(object, method, args);
		} else {
			return ReflectUtil.invokeMehod(object, method);
		}
	}

	@Override
	public void destroy() {
	}

}
