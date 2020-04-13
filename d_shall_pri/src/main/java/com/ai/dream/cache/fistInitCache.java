package com.ai.dream.cache;

import com.ai.dream.config.Constants;
import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;


@Component
@Order()
public class fistInitCache implements CommandLineRunner {
    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private IResourceShallSV isv;

    @Override
    public void run(String... args) throws Exception {
        //启动时，从数据库取资源库存
        Map<String,Object> map = new HashMap<>();
        map.put("id", Constants.resourceKey.RED_PACKET_ID);
        Map<String,Object> temp = isv.getKuc(map);
        int count = (int) temp.get("res_count");
        jedisCluster.set("redBag",String.valueOf(count));
        jedisCluster.set("staticCount",String.valueOf(count));
        System.out.println("redBag:"+count);
    }
}
