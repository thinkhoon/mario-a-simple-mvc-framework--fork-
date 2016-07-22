package com.junicorn.tkhoon;

import java.lang.reflect.Method;

import com.junicorn.tkhoon.config.ConfigLoader;
import com.junicorn.tkhoon.render.JspRender;
import com.junicorn.tkhoon.render.Render;
import com.junicorn.tkhoon.route.Routers;
import com.junicorn.tkhoon.servlet.wrapper.Request;
import com.junicorn.tkhoon.servlet.wrapper.Response;

/**
 * TKHooN
 * @author biezhi
 *
 */
public final class TKHooN {

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
	
	private TKHooN() {
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
	
	private static class TKHonnNHolder {
		private static TKHooN ME = new TKHooN();
	}
	
	public static TKHooN me(){
		return TKHonnNHolder.ME;
	}
	
	public TKHooN loadConf(String conf){
		configLoader.load(conf);
		return this;
	}
	
	public TKHooN setConf(String name, String value){
		configLoader.setConf(name, value);
		return this;		 
	}
	
	public String getConf(String name){
		return configLoader.getConf(name);
	}
	
	public TKHooN addRoutes(Routers routers){
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
	public TKHooN addRoute(String path, String methodName, Object controller){
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
