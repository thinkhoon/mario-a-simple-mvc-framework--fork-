package com.junicorn.mario.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

/**
 * 数据库支持
 * @author biezhi
 *
 */
public final class MarioDb {
	
	private static Sql2o sql2o = null;
	
	private MarioDb() {
	}
	
	/**
	 * 初始化数据库配置
	 * @param url
	 * @param user
	 * @param pass
	 */
	public static void init(String url, String user, String pass){
		sql2o = new Sql2o(url, user, pass);
	}
	
	/**
	 * 初始化数据库配置
	 * @param dataSource
	 */
	public static void init(DataSource dataSource){
		sql2o = new Sql2o(dataSource);
	}
	
	/**
	 * 查询一个对象
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T> T get(String sql, Class<T> clazz){
		return get(sql, clazz, null);
	}
	
	/**
	 * 查询一个列表
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(String sql, Class<T> clazz){
		return getList(sql, clazz, null);
	}
	
	/**
	 * 查询一个对象返回为map类型
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> getMap(String sql){
		return getMap(sql, null);
	}
	
	/**
	 * 查询一个列表并返回为list<map>类型
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> getMapList(String sql){
		return getMapList(sql, null);
	}
	
	/**
	 * 插入一条记录
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int insert(String sql, Object ... params){
		StringBuffer sqlBuf = new StringBuffer(sql);
		sqlBuf.append(" values (");
		
		int start = sql.indexOf("(") + 1;
		int end = sql.indexOf(")");
		String a = sql.substring(start, end);
		String[] fields = a.split(",");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int i=0;
		for(String name : fields){
			sqlBuf.append(":" + name.trim() + " ,");
			map.put(name.trim(), params[i]);
			i++;
		}
		
		String newSql = sqlBuf.substring(0, sqlBuf.length() - 1) + ")";
		
		Connection con = sql2o.open();
		Query query = con.createQuery(newSql);
		
		executeQuery(query, map);
		
		int res = query.executeUpdate().getResult();
		
		con.close();
		
		return res;
	}
	/**
	 * 更新
	 * @param sql
	 * @return
	 */
	public static int update(String sql){
		return update(sql, null);
	}
	
	/**
	 * 带参数更新
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(String sql, Map<String, Object> params){
		Connection con = sql2o.open();
		Query query = con.createQuery(sql);
		executeQuery(query, params);
		int res = query.executeUpdate().getResult();
		con.close();
		return res;
	}
	
	public static <T> T get(String sql, Class<T> clazz, Map<String, Object> params){
		Connection con = sql2o.open();
		Query query = con.createQuery(sql);
		executeQuery(query, params);
		T t = query.executeAndFetchFirst(clazz);
		con.close();
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(String sql, Map<String, Object> params){
		Connection con = sql2o.open();
		Query query = con.createQuery(sql);
		executeQuery(query, params);
		Map<String, Object> t = (Map<String, Object>) query.executeScalar();
		con.close();
		return t;
	}
	
	public static List<Map<String, Object>> getMapList(String sql, Map<String, Object> params){
		Connection con = sql2o.open();
		Query query = con.createQuery(sql);
		executeQuery(query, params);
		List<Map<String, Object>> t = query.executeAndFetchTable().asList();
		con.close();
		return t;
	}
	
	public static <T> List<T> getList(String sql, Class<T> clazz, Map<String, Object> params){
		Connection con = sql2o.open();
		Query query = con.createQuery(sql);
		executeQuery(query, params);
		List<T> list = query.executeAndFetch(clazz);
		con.close();
		return list;
	}
	
	private static void executeQuery(Query query, Map<String, Object> params){
		if (null != params && params.size() > 0) {
			Set<String> keys = params.keySet();
			for(String key : keys){
				query.addParameter(key, params.get(key));
			}
		}
	}
}
