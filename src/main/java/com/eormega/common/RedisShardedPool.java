package com.eormega.common;

import com.eormega.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;// sharded jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//在Jedispool中最大的idle状态(空闲的)的jedis实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//在Jedispool中最小的idle状态(空闲的)的jedis实例个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果是true，测试可用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//return，是否要进行验证操作，如果是true，测试可用的

    private static String redisIp1 = PropertiesUtil.getProperty("redis.ip1");
    private static Integer redisPort1 = Integer.parseInt(PropertiesUtil.getProperty("redis.port1"));
    private static String redisIp2 = PropertiesUtil.getProperty("redis.ip2");
    private static Integer redisPort2 = Integer.parseInt(PropertiesUtil.getProperty("redis.port2"));




    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时，是否阻塞，false会抛出异常，true阻塞知道超时。默认为true

        JedisShardInfo info1 = new JedisShardInfo(redisIp1,redisPort1,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redisIp2,redisPort2,1000*2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config,jedisShardInfoList,Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);


    }

    static {
        initPool();
    }
    public static ShardedJedis getJedis(){
        return pool.getResource();
    }
    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }
    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0 ; i < 10; i++){
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);
        //pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
