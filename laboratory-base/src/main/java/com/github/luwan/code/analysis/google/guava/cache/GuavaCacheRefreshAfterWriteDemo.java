package com.github.luwan.code.analysis.google.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hechao
 * @date 2020/4/19
 */
public class GuavaCacheRefreshAfterWriteDemo {

    private static AtomicInteger value = new AtomicInteger();

    private static Executor threadPool = Executors.newFixedThreadPool(1);

    private static LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .refreshAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " load...");
                    TimeUnit.SECONDS.sleep(1);
                    return value.getAndIncrement();
                }

                @Override
                public ListenableFuture<Integer> reload(final String key, Integer oldValue) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " reload...");
                    ListenableFutureTask<Integer> task = ListenableFutureTask.create(() -> load(key));
                    threadPool.execute(task);
                    return task;
                }
            });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String key = "key";
        // 初始化默认占位符
        System.out.println(cache.get(key, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return -1;
            }
        }));
        // 模拟请求初始值
        System.out.println(cache.get(key));

        // 缓存过期
        TimeUnit.SECONDS.sleep(6);

        // 触发触发异步刷新，且当前线程直接返回旧值
        System.out.println(cache.get(key));

        // 模拟其他线程在刷新未完成前返回旧值
        System.out.println(cache.get(key));

        // 模拟异步刷新完成后取得新值
        TimeUnit.SECONDS.sleep(2);
        System.out.println(cache.get(key));

        System.out.println(cache.asMap());
    }

}
