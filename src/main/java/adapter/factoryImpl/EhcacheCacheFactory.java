package adapter.factoryImpl;

import adapter.ICoreCache;
import adapter.ICoreCacheFactory;
import adapter.cacheImpl.EhcacheCache;
import common.CacheLogger;
import common.CoreCacheConstant;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 * @description Ehcache 缓存对象创建工厂接口实现类
 */
public class EhcacheCacheFactory implements ICoreCacheFactory {

    @Override
    public ICoreCache createCache() {
        ICoreCache ehcacheCache = new EhcacheCache();
        CacheManager cacheManager;
        Cache cache = null;
        // 尝试读取外部配置文件获取缓存对象
        InputStream is = EhcacheCacheFactory.class.getClassLoader().getResourceAsStream(CoreCacheConstant.EHCACHE_OUTER_SETTING_FILENAME);
        if (is != null) {
            cacheManager = CacheManager.newInstance(is);
            cache = cacheManager.getCache(CoreCacheConstant.EHCACHE_CACHE_USE_NAME);
            if (cache == null) {
                CacheLogger.loger.log(Level.WARNING, "The cache configuration item with name " + CoreCacheConstant.EHCACHE_CACHE_USE_NAME + " could not be found in the " + CoreCacheConstant.EHCACHE_OUTER_SETTING_FILENAME + " file.", new RuntimeException());
                // 外部配置文件没有编写 CoreCache 缓存对象，使用组件内部默认的 CoreCache 缓存对象配置
                cache = createDefaultCache();
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                CacheLogger.loger.log(Level.WARNING, e.getMessage(), e);
            }
        } else {
            // 没有编写外部配置文件，使用组件内置默认配置文件配置项
            cache = createDefaultCache();
        }

        try {
            Field field = ehcacheCache.getClass().getDeclaredField(CoreCacheConstant.EHCACHE_CACHE_FIELD_NAME);
            field.setAccessible(true);
            field.set(ehcacheCache, cache);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            CacheLogger.loger.log(Level.SEVERE, "The " + CoreCacheConstant.EHCACHE_CACHE_FIELD_NAME + " injection into ehcacheCache failed：" + e.getMessage(), e);
            throw new RuntimeException("The " + CoreCacheConstant.EHCACHE_CACHE_FIELD_NAME + " injection into ehcacheCache failed：" + e.getMessage());
        }

        return ehcacheCache;
    }

    private Cache createDefaultCache() {
        Cache cache = null;
        try (InputStream is = EhcacheCacheFactory.class.getClassLoader().getResourceAsStream(CoreCacheConstant.EHCACHE_DEFAULT_SETTING_FILENAME)) {
            CacheManager cacheManager = CacheManager.newInstance(is);
            cache = cacheManager.getCache(CoreCacheConstant.EHCACHE_CACHE_USE_NAME);
        } catch (IOException e) {
            CacheLogger.loger.log(Level.SEVERE, "The " + CoreCacheConstant.EHCACHE_CACHE_FIELD_NAME + " injection into ehcacheCache failed：" + e.getMessage(), e);
        }
        return cache;
    }

}
