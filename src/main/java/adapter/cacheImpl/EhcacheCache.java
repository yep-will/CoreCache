package adapter.cacheImpl;

import adapter.ICoreCache;
import common.CacheLogger;
import common.CoreCacheConstant;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @description Ehcache 客户端适配层
 */
public class EhcacheCache implements ICoreCache {

    private Cache cache;

    @Override
    public int set(Object key, Object value) {
        try {
            cache.put(new Element(key, value));
        } catch (Exception e) {
            CacheLogger.loger.log(Level.SEVERE, "Ehcache put element exception：" + e.getMessage(), e);
            return CoreCacheConstant.EXITCODE_FAILED;
        }
        return CoreCacheConstant.EXITCODE_SUCCESS;
    }

    @Override
    public int set(Object key, Object value, long expireTime, TimeUnit timeUnit) {
        Element element = new Element(key, value);
        int seconds = (int) (timeUnit.toSeconds(expireTime));
        element.setTimeToLive(seconds);
        try {
            cache.put(element);
        } catch (Exception e) {
            CacheLogger.loger.log(Level.SEVERE, "Ehcache put element exception：" + e.getMessage(), e);
            return CoreCacheConstant.EXITCODE_FAILED;
        }
        return CoreCacheConstant.EXITCODE_SUCCESS;
    }

    @Override
    public <T> T get(Object key, Class<T> tClass) {
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        T value = (T) element.getObjectValue();
        if (value != null && tClass.isAssignableFrom(value.getClass())) {
            return value;
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(Object key) {
        return cache.isElementInMemory(key) ||
                cache.isElementOffHeap(key) ||
                cache.isElementOnDisk(key);
    }

    @Override
    public boolean remove(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    @Override
    public long size() {
        return cache.getSize();
    }

    @Override
    public boolean setExpirationTime(Object key, long expireTime, TimeUnit timeUnit) {
        Element element = cache.get(key);
        if (element == null) {
            return false;
        }
        element.setTimeToLive((int) (timeUnit.toSeconds(expireTime)));
        try {
            cache.put(element);
        } catch (Exception e) {
            CacheLogger.loger.log(Level.SEVERE, "Ehcache put element exception：" + e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public boolean persist() {
        CacheLogger.loger.log(Level.WARNING, "CoreCache's built-in Ehcache does not currently support persistence.", new RuntimeException());
        return false;
    }

}
