import adapter.ICoreCache;
import external.CoreCacheManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pojo.User;

import java.util.concurrent.TimeUnit;

/**
 * @description 测试类
 */
public class CacheTest {

    static ICoreCache testCache;

    @BeforeAll
    public static void InjectRedis() {
        testCache = CoreCacheManager.getLocalCache();
    }

    @Test
    public void testSet() {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        Integer i = testCache.set("testUser", user);
        Assertions.assertEquals(1, i);
    }

    @Test
    public void testSetWithExpires() throws InterruptedException {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        Integer i = testCache.set("testUser", user, 5, TimeUnit.SECONDS);
        Assertions.assertEquals(1, i);

        TimeUnit.SECONDS.sleep(4);
        User user2 = testCache.get("testUser", User.class);
        Assertions.assertEquals(user.getName(), user2.getName());

        TimeUnit.SECONDS.sleep(2);
        User user3 = testCache.get("testUser", User.class);
        Assertions.assertNull(user3);
    }

    @Test
    public void testGet() {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        testCache.set("testUser", user);
        User user2 = testCache.get("testUser", User.class);
        Assertions.assertEquals(user.getName(), user2.getName());
    }

    @Test
    public void testContains() {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        Assertions.assertFalse(testCache.contains("testUser"));

        testCache.set("testUser", user);
        Assertions.assertTrue(testCache.contains("testUser"));
    }

    @Test
    public void testRemove() {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        Assertions.assertFalse(testCache.remove("testUser"));

        testCache.set("testUser", user);
        Assertions.assertTrue(testCache.remove("testUser"));
    }

    @Test
    public void testClearAndSize() {
        testCache.remove("testUser1");
        testCache.remove("testUser2");
        User user1 = new User("mike", 18);
        User user2 = new User("Amy", 28);

        testCache.set("testUser1", user1);
        testCache.set("testUser2", user2);
        System.out.println("当前缓存对象数量：" + testCache.size());

        testCache.clear();
        Assertions.assertEquals(0, testCache.size());
    }

    @Test
    public void testSetExpirationTime() throws InterruptedException {
        testCache.remove("testUser");
        User user = new User("mike", 18);

        testCache.set("testUser", user);
        User user2 = testCache.get("testUser", User.class);
        Assertions.assertEquals(user.getName(), user2.getName());

        testCache.setExpirationTime("testUser", 5000L, TimeUnit.MILLISECONDS);
        TimeUnit.SECONDS.sleep(6);
        Assertions.assertNull(testCache.get("testUser", User.class));
    }

    @Test
    public void testPersist() {
        testCache.remove("testUser");
        User user = new User("mike", 18);
        testCache.set("testUser", user);
        Assertions.assertTrue(testCache.persist());
    }

}
