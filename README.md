# CoreCache 通用缓存组件

## 组件说明
CoreCache 作为缓存厂商的过渡层统一了不同缓存工具的缓存操作 API，需要变更缓存的实现仅需修改配置文件即可。CoreCache 组件内支持本地缓存和全局缓存两种级别的缓存对象。

## 快速开始
1. 引入组件依赖，如果使用组件内置的缓存适配，由于组件内置的 Ehcache 和 Redis 使用到了 Gson 进行对象转换，所以需要引入如下依赖（注意版本）：
   ```xml
       <dependencies>
           <dependency>
               <groupId>redis.clients</groupId>
               <artifactId>jedis</artifactId>
               <version>3.3.0</version>
           </dependency>
           <dependency>
               <groupId>net.sf.ehcache</groupId>
               <artifactId>ehcache</artifactId>
               <version>2.10.2</version>
           </dependency>
           <dependency>
               <groupId>com.google.code.gson</groupId>
               <artifactId>gson</artifactId>
               <version>2.10</version>
           </dependency>
       </dependencies>
   ```
2. 代码使用，基本使用方式如下：
   ```java
    public class Main {
        public static void main(String[] args) {
            ICoreCache cache = CoreCacheManager.getLocalCache();
            TestUser testUser = new TestUser("will", 18);
            cache.set("user1", testUser);
        }
    }
   ```
## 内置本地缓存说明

- 组件内置的本地缓存实现为 Ehcache，内置 cache 的参数为：
    ```xml
    <cache name="CoreCache"
         maxElementsInMemory="2000"
         eternal="false"
         timeToIdleSeconds="1800"
         timeToLiveSeconds="3600"
         overflowToDisk="true"
         diskPersistent="true"
         memoryStoreEvictionPolicy="LRU"/>
    ```
- 如果需要自定义 cache 参数，需要在您项目中的 resource 目录下，编写 coreehcache.xml 配置文件（具体配置项参考 Ehcache 官方文档），注意，组件只会读取其中 name 属性为 "CoreCache" 的 cache 对象，如果在您编写的 coreehcache.xml 配置文件中找不到 name 为 "CoreCache" 的 cache 对象，则依然使用默认的 cache 配置。

## 内置全局缓存说明

- 组件内置的全局缓存实现为 Jedis，默认相关参数如下：
    - host：localhost
    - port：6379
    - timeout：2000 ms
- 如果需要使用自定义 redis 参数，则需要在您项目中的 resource 目录下，编写 redis.properties 配置文件，示例如下：
    ```
    # Redis连接配置
    host=192.168.0.5
    port=6377
    timeout=3000
    password=123456
    ```

## 拓展缓存说明

如果您需要拓展 CoreCache 组件的缓存实现，具体步骤如下：

1. 项目的 resources 目录下编写 corecache.properties 配置文件，示例内容如下：
    ```
    local.use=Ehcache
    global.use=mycorecache.myFactory
    ```
   其中 local.use 如果不编写则默认使用 Ehcache，示例中的 mycorecache.myFactory 为您需要实现的自定义 factory 接口的类路径；
2. mycorecache 类路径下编写 myFactory 实现类，实现 ICoreCacheFactory 接口；
3. 在您编写的 myFactory 实现类中，需要 new 一个 ICoreCache 的实现类对象，以 jedis 为例，在 RedisCacheFactory 中需要 new 一个 jedis 通过反射的方式将其注入到 RedisCache 对象中，最后返回；
4. 而 RedisCache 需要实现 ICoreCache 接口，重写其中的方式完成适配；
5. 当您后续在代码中编写 CoreCacheManager.getGlobalCache(); 时，CoreCache 组件即会返回一个您自定义的缓存实现客户端。