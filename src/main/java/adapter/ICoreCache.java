package adapter;

import java.util.concurrent.TimeUnit;

/**
 * @description 缓存对象操作接口
 */
public interface ICoreCache {

    /**
     * 往ICoreCache中添加缓存
     */
    int set(Object key, Object value);

    /**
     * 往ICoreCache中添加缓存，包含过期时间
     */
    int set(Object key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 从ICoreCache中获取缓存
     */
    <T> T get(Object key, Class<T> tClass);

    /**
     * 判断ICoreCache中是否存在指定缓存
     */
    boolean contains(Object key);

    /**
     * 删除ICoreCache中指定缓存
     */
    boolean remove(Object key);

    /**
     * 清空ICoreCache中所有缓存
     */
    void clear();

    /**
     * 获取ICoreCache中缓存数量
     */
    long size();

    /**
     * 设置指定缓存过期时间
     */
    boolean setExpirationTime(Object key, long expireTime, TimeUnit timeUnit);

    /**
     * 持久化
     */
    boolean persist();

}
