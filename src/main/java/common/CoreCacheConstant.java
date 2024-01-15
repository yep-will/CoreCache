package common;

/**
 * @description CoreCache组件常量类
 */
public class CoreCacheConstant {

    /**
     * CoreCache 组件相关常量
     */
    public static final String CORECACHE_PROPERTIES_NAME = "corecache.properties";
    public static final String LOCAL_CACHE_KEY_NAME = "local.use";
    public static final String GLOBAL_CACHE_KEY_NAME = "global.use";
    public static final String LOCAL_CACHE_DEFAULT = "Ehcache";
    public static final String GLOBAL_CACHE_DEFAULT = "Redis";
    public static final int EXITCODE_SUCCESS = 1;
    public static final int EXITCODE_FAILED = 0;
    public static final String PACKAGE_OF_FACTORYCLASS = "adapter.factoryImpl.";
    public static final String CACHEFACTORY_SUFFIX = "CacheFactory";

    /**
     * Ehcache 配置相关常量
     */
    public static final String EHCACHE_DEFAULT_SETTING_FILENAME = "innercoreehcache.xml";
    public static final String EHCACHE_OUTER_SETTING_FILENAME = "coreehcache.xml";
    public static final String EHCACHE_CACHE_USE_NAME = "CoreCache";
    public static final String EHCACHE_CACHE_FIELD_NAME = "cache";

    /**
     * Redis 配置相关常量
     */
    public static final String REDIS_SETTING_FILENAME = "redis.properties";
    public static final String REDIS_CACHE_FIELD_NAME = "jedis";
    public static final String REDIS_CONN_DEFAULT_HOST = "localhost";
    public static final String REDIS_CONN_DEFAULT_PORT = "6379";
    public static final String REDIS_CONN_DEFAULT_TIMEOUT = "2000";
    public static final String REDIS_RESP_SUCCESS = "OK";

}
