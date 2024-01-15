package adapter.factoryImpl;

import adapter.ICoreCache;
import adapter.ICoreCacheFactory;
import adapter.cacheImpl.RedisCache;
import common.CoreCacheConstant;
import redis.clients.jedis.Jedis;
import common.CacheLogger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Level;

/**
 * @description redis 缓存对象创建工厂接口实现类
 */
public class RedisCacheFactory implements ICoreCacheFactory {

    @Override
    public ICoreCache createCache() {
        ICoreCache redisCache = new RedisCache();

        // 尝试读取外部配置文件生成 jedis
        Properties pro = new Properties();

        try (InputStream is = RedisCacheFactory.class.getClassLoader().getResourceAsStream(CoreCacheConstant.REDIS_SETTING_FILENAME)) {
            if (is != null) {
                // 用户配置了 redis.properties 配置文件
                pro.load(is);
            }
        } catch (IOException e) {
            CacheLogger.loger.log(Level.WARNING, "RedisCacheFactory Load external " + CoreCacheConstant.REDIS_SETTING_FILENAME + " exception：" + e.getMessage(), e);
        }
        Jedis jedis = initJedis(pro);

        // 将 jedis 注入 redisCache 中
        try {
            Field field = redisCache.getClass().getDeclaredField(CoreCacheConstant.REDIS_CACHE_FIELD_NAME);
            field.setAccessible(true);
            field.set(redisCache, jedis);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            CacheLogger.loger.log(Level.SEVERE, "The " + CoreCacheConstant.REDIS_CACHE_FIELD_NAME + " injection into redisCache failed：" + e.getMessage(), e);
            throw new RuntimeException("The " + CoreCacheConstant.REDIS_CACHE_FIELD_NAME + " injection into redisCache failed：" + e.getMessage());
        }

        return redisCache;
    }

    private Jedis initJedis(Properties pro) {
        Jedis jedis = new Jedis(
                pro.getProperty("host", CoreCacheConstant.REDIS_CONN_DEFAULT_HOST),
                Integer.parseInt(pro.getProperty("port", CoreCacheConstant.REDIS_CONN_DEFAULT_PORT)),
                Integer.parseInt(pro.getProperty("timeout", CoreCacheConstant.REDIS_CONN_DEFAULT_TIMEOUT))
        );
        String pwd = pro.getProperty("password");
        if (pwd != null && pwd.length() > 0) {
            jedis.auth(pwd);
        }

        return jedis;
    }

}
