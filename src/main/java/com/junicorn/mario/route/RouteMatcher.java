package com.junicorn.mario.route;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.junicorn.mario.util.PathUtil;

/**
 * 路由匹配器，用于匹配路由
 * @author biezhi
 */
public class RouteMatcher {

	private List<Route> routes;

	public RouteMatcher(List<Route> routes) {
		this.routes = routes;
	}
	
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	/**
	 * 根据path查找路由
	 * @param path	请求地址
	 * @return		返回查询到的路由
	 */
	public Route findRoute(String path) {
		String cleanPath = parsePath(path);
		List<Route> matchRoutes = new ArrayList<Route>();
		for (Route route : this.routes) {
			if (matchesPath(route.getPath(), cleanPath)) {
				matchRoutes.add(route);
			}
		}
		// 优先匹配原则
        giveMatch(path, matchRoutes);
        
        return matchRoutes.size() > 0 ? matchRoutes.get(0) : null;
	}

	private void giveMatch(final String uri, List<Route> routes) {
		//如果能找到完全匹配url的route，那么就使用这个route，否则保持排序顺序不变
		Collections.sort(routes, new Comparator<Route>() {
			@Override
			public int compare(Route o1, Route o2) {
				if (o2.getPath().equals(uri)) {
					return o2.getPath().indexOf(uri);
				}
				return -1;
			}
		});
	}
	
	private boolean matchesPath(String routePath, String pathToMatch) {
		//（？i）表达忽略大消息   i  忽略的英文首字母。
		// replaceAll  也是一个可以匹配正则表达式的函数。
		routePath = routePath.replaceAll(PathUtil.VAR_REGEXP, PathUtil.VAR_REPLACE);
		return pathToMatch.matches("(?i)" + routePath );
	}

	private String parsePath(String path) {
		path = PathUtil.fixPath(path);
		try {
			URI uri = new URI(path);
			return uri.getPath();
		} catch (URISyntaxException e) {
			return null;
		}
	}

}