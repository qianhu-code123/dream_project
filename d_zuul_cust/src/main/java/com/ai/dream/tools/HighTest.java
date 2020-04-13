package com.ai.dream.tools;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

//测试1W人抢100个资源，用redis分布式锁实现，不能两人抢一个情况
public class HighTest {

    @Autowired
    private JedisCluster jedisCluster;




}
