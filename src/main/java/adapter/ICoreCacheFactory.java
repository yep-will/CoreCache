package adapter;

/**
 * @description 缓存对象创建工厂接口
 */
public interface ICoreCacheFactory {

    /**
     * 创建对应ICoreCache对象
     */
    ICoreCache createCache();

}
