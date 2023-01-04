package cn.javaex.htool.cache;

import java.util.Map;
import java.util.Set;

import cn.javaex.htool.cache.manager.impl.CacheManagerImpl;
import cn.javaex.htool.cache.model.CacheInfo;

/**
 * 缓存工具类
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class CacheUtils {

	/**
	 * 写入缓存
	 * @param key : 缓存key
	 * @param cacheData ： 缓存内容
	 * @param expireTime : 过期时间，单位（毫秒），为0时表示不失效
	 */
	public static void setCache(String key, Object cacheData, long expireTime) {
		new CacheManagerImpl().setCache(key, cacheData, expireTime);
	}
	
	/**
	 * 读取缓存
	 * @param key : 缓存key
	 * @return
	 */
	public static Object getCache(String key) {
		return new CacheManagerImpl().getCache(key);
	}
	
	/**
	 * 清除所有缓存
	 */
	public static void removeAll() {
		new CacheManagerImpl().removeAll();
	}
	
	/**
	 * 清除指定key的缓存
	 * @param key : 缓存key
	 */
	public static void removeByKey(String key) {
		new CacheManagerImpl().removeByKey(key);
	}
	
	/**
	 * 获取所有缓存key
	 * @return
	 */
	public static Set<String> getAllKeys() {
		return new CacheManagerImpl().getAllKeys();
	}
	
	/**
	 * 获取所有缓存信息
	 */
	public static Map<String, CacheInfo> getAllCacheInfos() {
		return new CacheManagerImpl().getAllCacheInfos();
	}

	/**
	 * 获取指定key的缓存信息
	 * @param key : 缓存key
	 * @return
	 */
	public static CacheInfo getCacheInfo(String key) {
		return new CacheManagerImpl().getCacheInfo(key);
	}
	
}
