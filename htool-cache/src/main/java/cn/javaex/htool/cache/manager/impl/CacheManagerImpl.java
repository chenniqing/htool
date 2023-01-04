package cn.javaex.htool.cache.manager.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.javaex.htool.cache.manager.ICacheManager;
import cn.javaex.htool.cache.model.CacheInfo;
import cn.javaex.htool.cache.task.CacheClearTask;

/**
 * 缓存管理
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class CacheManagerImpl implements ICacheManager {
	private static final long serialVersionUID = 1L;
	
	public static Map<String, CacheInfo> cacheMap = new ConcurrentHashMap<String, CacheInfo>();
	
	static {
		CacheClearTask cacheTask = new CacheClearTask(new CacheManagerImpl());
		cacheTask.clearSchedule();
	}
	
	/**
	 * 写入缓存
	 * @param key
	 * @param cacheData
	 * @param timeout
	 */
	@Override
	public void setCache(String key, Object cacheData, long expireTime) {
		if (expireTime==0) {
			expireTime = 0L;
		}
		
		cacheMap.put(key, new CacheInfo(cacheData, expireTime, System.currentTimeMillis()));
	}
	
	/**
	 * 获取缓存数据
	 * @param key
	 * @return
	 */
	@Override
	public Object getCache(String key) {
		if (!this.isExistsKey(key)) {
			return null;
		}
		
		if (this.isExpire(key)) {
			this.removeByKey(key);
			return null;
		}
		
		return cacheMap.get(key).getData();
	}

	/**
	 * 检查给定 key 是否存在
	 * @param key
	 * @return
	 */
	@Override
	public boolean isExistsKey(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * 清除所有缓存
	 */
	@Override
	public void removeAll() {
		cacheMap.clear();
	}

	/**
	 * 清除对应缓存
	 * @param key
	 */
	@Override
	public void removeByKey(String key) {
		if (this.isExistsKey(key)) {
			cacheMap.remove(key);
		}
	}

	/**
	 * 判断缓存是否超时失效（true表示已失效）
	 * @param key
	 * @return
	 */
	@Override
	public boolean isExpire(String key) {
		if (!cacheMap.containsKey(key)) {
			return true;
		}
		
		CacheInfo cache = cacheMap.get(key);
		long expireTime = cache.getExpireTime();
		if (expireTime==0) {
			return false;
		}
		
		long lastRefreshTime = cache.getLastRefeshTime();
		if ((System.currentTimeMillis()-lastRefreshTime) >= expireTime) {
			return true;
		}
		
		return false;
	}

	/**
	 * 获取所有key
	 * @return
	 */
	@Override
	public Set<String> getAllKeys() {
		return cacheMap.keySet();
	}

	/**
	 * 获取所有缓存信息
	 */
	@Override
	public Map<String, CacheInfo> getAllCacheInfos() {
		return cacheMap;
	}

	/**
	 * 获取缓存信息
	 */
	@Override
	public CacheInfo getCacheInfo(String key) {
		if (this.isExistsKey(key)) {
			return cacheMap.get(key);
		}
		return null;
	}
	
}
