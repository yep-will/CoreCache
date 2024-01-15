package config;

import common.CacheLogger;
import common.CoreCacheConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * @description 管理组件配置文件配置项（corecache.properties）
 */
public class ConfigReader {

    /**
     * 组件配置项
     */
    private static Properties configPro = new Properties();

    static {
        initDefaultConfig();
        initExternalConfig();
    }

    /**
     * 加载组件内部配置文件数据（这里暂时硬编码，不采用配置文件形式）
     */
    private static void initDefaultConfig() {
        configPro.setProperty(CoreCacheConstant.LOCAL_CACHE_KEY_NAME, CoreCacheConstant.LOCAL_CACHE_DEFAULT);
        configPro.setProperty(CoreCacheConstant.GLOBAL_CACHE_KEY_NAME, CoreCacheConstant.GLOBAL_CACHE_DEFAULT);
    }

    /**
     * 加载组件外部 corecache.properteis 配置文件数据
     */
    private static void initExternalConfig() {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CoreCacheConstant.CORECACHE_PROPERTIES_NAME)) {
            if (inputStream != null) {
                configPro.load(inputStream);
            }
        } catch (IOException e) {
            CacheLogger.loger.log(Level.SEVERE, "Loading component external " + CoreCacheConstant.CORECACHE_PROPERTIES_NAME + " configuration file data exception：" + e.getMessage(), e);
        }
    }

    /**
     * 获取组件（内外）配置文件数据
     */
    public static Properties getConfigPro() {
        if (configPro.isEmpty()) {
            CacheLogger.loger.log(Level.SEVERE, "CoreCache Properties exception(is null).", new RuntimeException());
            throw new RuntimeException("CoreCache Properties exception(is null).");
        }
        return configPro;
    }

}
