package cn.javaex.htool.cache.task;

import java.util.Timer;
import java.util.TimerTask;

import cn.javaex.htool.cache.manager.impl.CacheManagerImpl;

/**
 * 自动清理缓存
 * 
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class CacheClearTask {
	
	private CacheManagerImpl cacheManagerImpl;
	
	public CacheClearTask(CacheManagerImpl cacheManagerImpl) {
		this.cacheManagerImpl = cacheManagerImpl;
	}

	/**
	 * 删除过期缓存（每隔5分钟循环检测一次）
	 */
	public void clearSchedule() {
		new Timer().schedule(new TimerTask() { 
			@Override
			public void run() { 
				this.refresh();
			}
			
			private void refresh() {
				for (String key : CacheManagerImpl.cacheMap.keySet()) { 
					if (cacheManagerImpl.isExpire(key)) { 
						cacheManagerImpl.removeByKey(key);
					}
				}
			}
		}, 0, 300000);
	}
}
