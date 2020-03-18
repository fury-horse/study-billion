package com.msb.study;


import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
* @desc    
* @version 1.0
* @author  Liang Jun
* @date    2020年03月10日 13:18:22
**/
public class LimiterTokenBucket {
    //稳定模式
    /*
    public static void main(String[] args) throws InterruptedException {
        // RateLimiter.create(2)每秒产生的令牌数
        RateLimiter limiter = RateLimiter.create(2);
        // limiter.acquire() 阻塞的方式获取令牌
        System.out.println(limiter.acquire());;
        System.out.println(limiter.acquire());;
        System.out.println(limiter.acquire());;
    }
    */

    public static void main(String[] args) {
        // 平滑限流，从冷启动速率（满的）到平均消费速率的时间间隔
        RateLimiter limiter = RateLimiter.create(1,1000l, TimeUnit.MILLISECONDS);
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
    }
}