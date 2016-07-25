package com.junicorn.mario.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 路由管理器，存放所有路由的
 * @author biezhi
 */
public class Routers {

	private static final Logger LOGGER = Logger.getLogger(Routers.class.getName());
	
	private List<Route> routes = new ArrayList<Route>();
	
	public Routers() {
	}
	
	public void addRoute(List<Route> routes){
		routes.addAll(routes);
	}
	
	public void addRoute(Route route){
		routes.add(route);
	}
	
	public void removeRoute(Route route){
		routes.remove(route);
	}
	
	public void addRoute(String path, Method action, Object controller){
		Route route = new Route();
		route.setPath(path);
		route.setAction(action);
		route.setController(controller);
		
		routes.add(route);
		LOGGER.info("Add Route：[" + path + "]");
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
}
