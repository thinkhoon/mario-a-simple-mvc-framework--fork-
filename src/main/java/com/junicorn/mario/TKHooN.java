package com.junicorn.mario;

import java.lang.reflect.Method;

import com.junicorn.mario.config.ConfigLoader;
import com.junicorn.mario.render.JspRender;
import com.junicorn.mario.render.Render;
import com.junicorn.mario.route.Routers;
import com.junicorn.mario.servlet.wrapper.Request;
import com.junicorn.mario.servlet.wrapper.Response;

/**
 * Mario
 * @author biezhi
 *
 */
public final class Mario {

	/**
	 * 存放所有路由
	 */
	private Routers routers;
	
	/**
	 * 配置加载器
	 */
	private ConfigLoader configLoader;
	
	/**
	 * 框架是否已经初始化
	 */
	private boolean init = false;
	
	/**
	 * 渲染器
	 */
	private Render render;
	
	private Mario() {
		routers = new Routers();
		configLoader = new ConfigLoader();
		render = new JspRender();
	}
	
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
	// 线程安全的单例模式的写法
	// 在classload的时候只会有一个线程，所以不会出现多线程的问题。
	private static class MarioHolder {
		private static Mario ME = new Mario();
	}
	
	public static Mario me(){
		return MarioHolder.ME;
	}
	
	public Mario loadConf(String conf){
		configLoader.load(conf);
		return this;
	}
	
	public Mario setConf(String name, String value){
		configLoader.setConf(name, value);
		return this;		 
	}
	
	public String getConf(String name){
		return configLoader.getConf(name);
	}
	
	public Mario addRoutes(Routers routers){
		this.routers.addRoute(routers.getRoutes());
		return this;
	}

	public Routers getRouters() {
		return routers;
	}
	
	/**
	 * 添加路由
	 * @param path			映射的PATH
	 * @param methodName	方法名称
	 * @param controller	控制器对象
	 * @return				返回Mario
	 */
	public Mario addRoute(String path, String methodName, Object controller){
		try {
			Method method = controller.getClass().getMethod(methodName, Request.class, Response.class);
			this.routers.addRoute(path, method, controller);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Render getRender() {
		return render;
	}

	public void setRender(Render render) {
		this.render = render;
	}
	
}
