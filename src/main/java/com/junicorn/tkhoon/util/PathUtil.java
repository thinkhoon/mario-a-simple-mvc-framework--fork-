package com.junicorn.tkhoon.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class PathUtil {

	public static final String VAR_REGEXP = ":(\\w+)";
	public static final String VAR_REPLACE = "([^#/?]+)";

	private static final String SLASH = "/";

	public static String getRelativePath(HttpServletRequest request) {

		String path = request.getRequestURI();
		String contextPath = request.getContextPath();

		path = path.substring(contextPath.length());

		if (path.length() > 0) {
			path = path.substring(1);
		}

		if (!path.startsWith(SLASH)) {
			path = SLASH + path;
		}

		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		return path;
	}
	
	public static boolean isParam(String routePart) {
		return routePart.startsWith(":");
	}

	public static boolean isSplat(String routePart) {
		return routePart.equals("*");
	}

	public static List<String> convertRouteToList(String route) {
        String[] pathArray = route.split("/");
        List<String> path = new ArrayList<String>();
        if(null != pathArray && pathArray.length > 0){
            for (String p : pathArray) {
                if (p.length() > 0) {
                    path.add(p);
                }
            }
        }
        return path;
    }
	
	public static String fixPath(String path) {
		if (path == null) {
			return "/";
		}
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (path.length() > 1 && path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		return path.replaceAll("[/]+", "/");
	}
}
