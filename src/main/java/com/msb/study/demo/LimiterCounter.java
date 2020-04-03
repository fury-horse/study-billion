package com.msb.study.demo;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月10日 13:01:42
**/
public class LimiterCounter {
    public static void main(String[] args) throws ExecutionException {
        long max = 100; //流量上限
        long unit = 1000;// 获得当前时间
        long current = System.currentTimeMillis() / unit; //判断时间窗内是否限制访问

        //计数器
        LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long secend) throws Exception {
                        return new AtomicLong(0);
                    }
                });
        if (counter.get(current).incrementAndGet() > max) {
            //拒绝
        }
        //放行
    }
}