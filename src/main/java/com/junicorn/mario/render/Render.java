package com.junicorn.mario.render;

import java.io.Writer;

/**
 * 视图渲染接口
 * @author biezhi
 *
 */
public interface Render {
	
	/**
	 * 渲染到视图
	 * @param view		视图名称
	 * @param writer	写入对象
	 */
	public void render(String view, Writer writer);
	
}
