package cn.javaex.htool.cache.model;

/**
 * 缓存信息
 *
 * @author 陈霓清
 * @Date 2022年11月25日
 */
public class CacheInfo {
    /**
     * 缓存数据
     */
    private Object data;
    /**
     * 失效时间：单位（毫秒）（0表示永不失效）
     */
    private long expireTime;
    /**
     * 最后刷新时间
     */
    private long lastRefeshTime;

    public CacheInfo(Object data, long expireTime, long lastRefeshTime) {
        this.data = data;
        this.expireTime = expireTime;
        this.lastRefeshTime = lastRefeshTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getLastRefeshTime() {
        return lastRefeshTime;
    }

    public void setLastRefeshTime(long lastRefeshTime) {
        this.lastRefeshTime = lastRefeshTime;
    }

    @Override
    public String toString() {
        return "CacheInfo [data=" + data + ", expireTime=" + expireTime + ", lastRefeshTime=" + lastRefeshTime + "]";
    }

}
