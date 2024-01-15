package adapter.cacheImpl;

import adapter.ICoreCache;
import common.CoreCacheConstant;
import common.Gsoner;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @description jedis 客户端适配层
 */
public class RedisCache implements ICoreCache {

    /**
     * jedis 客户端
     */
    private Jedis jedis;

    @Override
    public int set(Object key, Object value) {
        String jsonKey = Gsoner.gson.toJson(key);
        String jsonValue = Gsoner.gson.toJson(value);
        return CoreCacheConstant.REDIS_RESP_SUCCESS.equals(jedis.set(jsonKey, jsonValue)) ? CoreCacheConstant.EXITCODE_SUCCESS : CoreCacheConstant.EXITCODE_FAILED;
    }

    @Override
    public int set(Object key, Object value, long expireTime, TimeUnit timeUnit) {
        String jsonKey = Gsoner.gson.toJson(key);
        String jsonValue = Gsoner.gson.toJson(value);
        return CoreCacheConstant.REDIS_RESP_SUCCESS.equals(jedis.psetex(jsonKey, timeUnit.toMillis(expireTime), jsonValue)) ? CoreCacheConstant.EXITCODE_SUCCESS : CoreCacheConstant.EXITCODE_FAILED;
    }

    @Override
    public <T> T get(Object key, Class<T> tClass) {
        String jsonKey = Gsoner.gson.toJson(key);
        String jsonValue = jedis.get(jsonKey);
        return Gsoner.gson.fromJson(jsonValue, tClass);
    }

    @Override
    public boolean contains(Object key) {
        return jedis.exists(Gsoner.gson.toJson(key));
    }

    @Override
    public boolean remove(Object key) {
        return jedis.del(Gsoner.gson.toJson(key)) > 0;
    }

    @Override
    public void clear() {
        jedis.flushAll();
    }

    @Override
    public long size() {
        return jedis.dbSize();
    }

    @Override
    public boolean setExpirationTime(Object key, long expireTime, TimeUnit timeUnit) {
        String jsonKey = Gsoner.gson.toJson(key);
        return jedis.expire(jsonKey, (int) (timeUnit.toSeconds(expireTime))) > 0;
    }

    @Override
    public boolean persist() {
        return CoreCacheConstant.REDIS_RESP_SUCCESS.equals(jedis.save());
    }


}
