package com.eormega.util;


import com.eormega.common.RedisShardedPool;
import redis.clients.jedis.ShardedJedis;


public class RedisShardedPoolUtil {
    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }
    public static String getSet(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }
    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }



    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }


    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }


    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }
    public static Long setnx(String key,String value){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }
    // 保存byte类型数据
    public static String setObject(byte[] key, byte[] value, int exTime){
        ShardedJedis jedis = null;
        String result = null;
        if(jedis != null){
            try{
                jedis = RedisShardedPool.getJedis();
                result = jedis.setex(key,exTime,value);
            } catch(Exception e){
                e.printStackTrace();
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            } finally {
                RedisShardedPool.returnResource(jedis);
            }
        }
        return result;
    }
    // 获取byte类型数据
    public static byte[] getObject(byte[] key){
        ShardedJedis jedis = null;
        byte[] bytes = null;
        if(jedis != null){
            try{
                bytes = jedis.get(key);
            }catch(Exception e){
                e.printStackTrace();
                RedisShardedPool.returnBrokenResource(jedis);
                return bytes;
            }finally{
                RedisShardedPool.returnResource(jedis);
            }
        }
        return bytes;
    }

    // 更新byte类型的数据，主要更新过期时间
    public static void updateObject(byte[] key, int exTime){
        ShardedJedis jedis = null;
        if(jedis != null){
            try{
                // redis中session过期时间
                jedis.expire(key, exTime);
            }catch(Exception e){
                e.printStackTrace();
                RedisShardedPool.returnBrokenResource(jedis);
            } finally{
                RedisShardedPool.returnResource(jedis);
            }
        }
    }
    // 删除字符串数据
    public static void delString(String key){
        ShardedJedis jedis = null;
        if(jedis != null){
            try{
                jedis.del(key);
            }catch(Exception e){
                e.printStackTrace();
                RedisShardedPool.returnBrokenResource(jedis);
            } finally{
                RedisShardedPool.returnResource(jedis);
            }
        }
    }
    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisShardedPoolUtil.set("keyTest","value");
        String value = RedisShardedPoolUtil.get("keyTest");
        RedisShardedPoolUtil.setEx("keyex","valueex",60*10);
        RedisShardedPoolUtil.expire("keyTest",60*20);
        RedisShardedPoolUtil.del("keyTest");
    }
}
