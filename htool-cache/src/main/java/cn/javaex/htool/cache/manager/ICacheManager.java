package cn.javaex.htool.cache.manager;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import cn.javaex.htool.cache.model.CacheInfo;

/**
 * 缓存接口
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public interface ICacheManager extends Serializable {

	/**
	 * 写入缓存
	 * @param key
	 * @param cacheData
	 * @param timeOut
	 */
	void setCache(String key, Object cacheData, long timeOut);
	
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	Object getCache(String key);

	/**
	 * 检查给定 key 是否存在
	 * @param key
	 * @return
	 */
	boolean isExistsKey(String key);

	/**
	 * 清除所有缓存
	 */
	void removeAll();

	/**
	 * 清除对应缓存
	 * @param key
	 */
	void removeByKey(String key);

	/**
	 * 判断缓存是否超时失效（true表示已失效）
	 * @param key
	 * @return
	 */
	boolean isExpire(String key);

	/**
	 * 获取所有key
	 * @return
	 */
	Set<String> getAllKeys();
	
	/**
	 * 获取所有缓存信息
	 * @param key
	 * @return
	 */
	Map<String, CacheInfo> getAllCacheInfos();
	
	/**
	 * 获取缓存信息
	 * @param key
	 * @return
	 */
	CacheInfo getCacheInfo(String key);
	
}
