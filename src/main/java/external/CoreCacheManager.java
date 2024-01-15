package external;

import adapter.ICoreCache;
import adapter.ICoreCacheFactory;
import common.CacheLogger;
import common.CoreCacheConstant;
import config.ConfigReader;

import java.util.Properties;
import java.util.logging.Level;

/**
 * @description 对外服务，管理进程缓存和全局缓存对象
 */
public class CoreCacheManager {

    /**
     * 本地缓存操作对象
     */
    private static volatile ICoreCache localCache;

    /**
     * 全局缓存操作对象
     */
    private static volatile ICoreCache globalCache;

    private static final Object LOCALCACHE_LOCK = new Object();
    private static final Object GLOBALCACHE_LOCK = new Object();

    /**
     * 获取本地缓存操作对象
     */
    public static ICoreCache getLocalCache() {
        if (localCache == null) {
            synchronized (LOCALCACHE_LOCK) {
                if (localCache == null) {
                    localCache = createLocalCache();
                    if (localCache == null) {
                        String errorStr = "Initialization local cache operation object exception.";
                        CacheLogger.loger.log(Level.SEVERE, errorStr, new RuntimeException(errorStr));
                        throw new RuntimeException(errorStr);
                    }
                }
            }
        }
        return localCache;
    }

    /**
     * 获取全局缓存操作对象
     */
    public static ICoreCache getGlobalCache() {
        if (globalCache == null) {
            synchronized (GLOBALCACHE_LOCK) {
                if (globalCache == null) {
                    globalCache = createGlobalCache();
                    if (globalCache == null) {
                        String errorStr = "Initialization global cache operation object exception.";
                        CacheLogger.loger.log(Level.SEVERE, errorStr, new RuntimeException(errorStr));
                        throw new RuntimeException(errorStr);
                    }
                }
            }
        }
        return globalCache;
    }

    /**
     * 获取对应的缓存操作对象创建工厂
     */
    private static ICoreCacheFactory loadCoreCacheFactory(String type) {
        ICoreCacheFactory factory = null;

        // 拼接完整工厂名称（默认使用组件内部工厂）
        String factoryClassName = CoreCacheConstant.PACKAGE_OF_FACTORYCLASS + type + CoreCacheConstant.CACHEFACTORY_SUFFIX;

        // 优先从外部获取缓存对象创建工厂
        if (!CoreCacheConstant.LOCAL_CACHE_DEFAULT.equals(type) && !CoreCacheConstant.GLOBAL_CACHE_DEFAULT.equals(type)) {
            factoryClassName = type;
        }

        try {
            Class<?> factoryClass = Class.forName(factoryClassName);
            if (!ICoreCacheFactory.class.isAssignableFrom(factoryClass)) {
                CacheLogger.loger.log(Level.SEVERE, "The custom CacheFactory class does not implement ICoreCacheInterface");
                throw new RuntimeException("The custom CacheFactory class does not implement ICoreCacheInterface");
            }
            factory = (ICoreCacheFactory) factoryClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            CacheLogger.loger.log(Level.SEVERE, "CoreCache load " + factoryClassName + " exception：" + e.getMessage(), e);
        }

        return factory;
    }

    /**
     * 加载本地缓存操作对象
     */
    private static ICoreCache createLocalCache() {
        Properties pro = ConfigReader.getConfigPro();
        String localCacheName = pro.getProperty(CoreCacheConstant.LOCAL_CACHE_KEY_NAME);
        if (localCacheName == null || localCacheName.isEmpty()) {
            localCacheName = CoreCacheConstant.LOCAL_CACHE_DEFAULT;
        }
        ICoreCacheFactory localCacheFactory = loadCoreCacheFactory(localCacheName);
        ICoreCache icc = localCacheFactory.createCache();
        if (icc == null) {
            String errorMessage = "Initialization local cache operation object exception.";
            CacheLogger.loger.log(Level.SEVERE, errorMessage, new RuntimeException(errorMessage));
            throw new RuntimeException(errorMessage);
        }
        return icc;
    }

    /**
     * 生产全局缓存操作对象
     */
    private static ICoreCache createGlobalCache() {
        Properties pro = ConfigReader.getConfigPro();
        String globalCacheName = pro.getProperty(CoreCacheConstant.GLOBAL_CACHE_KEY_NAME);
        if (globalCacheName == null || globalCacheName.isEmpty()) {
            globalCacheName = CoreCacheConstant.GLOBAL_CACHE_DEFAULT;
        }
        ICoreCacheFactory globalCacheFactory = loadCoreCacheFactory(globalCacheName);
        ICoreCache icc = globalCacheFactory.createCache();
        if (icc == null) {
            String errorMessage = "Initialization global cache operation object exception.";
            CacheLogger.loger.log(Level.SEVERE, errorMessage, new RuntimeException(errorMessage));
            throw new RuntimeException(errorMessage);
        }
        return icc;
    }

}
