package com.john.doe.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by joker on 2022/12/7.
 */
public class CacheEvictionTest {

    @Test
    // 测试缓存被动淘汰策略-根据容量删除
    public void passiveEvictionBySize() {
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).removalListener(notification -> {
            System.out.println("监听到缓存移除事件：" + notification);
        }).build();
        System.out.println("put放入key1");
        cache.put("key1", "value1");

        System.out.println("put放入key2");
        cache.put("key2", "value1");

        System.out.println("put放入key3");
        cache.put("key3", "value1");

        System.out.println("put操作后，当前缓存记录数：" + cache.size());
        System.out.println("查询key1对应值：" + cache.getIfPresent("key1"));
    }

    @Test
    // 测试缓存被动淘汰策略-根据时间删除
    public void passiveEvictionByTime() throws InterruptedException {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                // .concurrencyLevel(1)
                .expireAfterWrite(1L, TimeUnit.SECONDS).recordStats().build();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        System.out.println("put操作后，当前缓存记录数：" + cache.size());
        System.out.println("查询key1对应值：" + cache.getIfPresent("key1"));
        System.out.println("统计信息：" + cache.stats());
        System.out.println("-------sleep 等待超过过期时间-------");
        Thread.sleep(1100L);
        System.out.println("执行key1查询操作：" + cache.getIfPresent("key1"));
        System.out.println("当前缓存记录数：" + cache.size());
        System.out.println("当前统计信息：" + cache.stats());
        System.out.println("剩余数据信息：" + cache.asMap());

    }
}
