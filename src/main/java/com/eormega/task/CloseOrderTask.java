package com.eormega.task;

import com.eormega.common.Const;
import com.eormega.util.PropertiesUtil;
import com.eormega.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloseOrderTask {

//    @Scheduled(cron = "0 0 0 */1 * ?")//everyday
    public void closeOrderTask(){
        log.info("关闭订单任务启动");
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
        if(setnxResult != null && setnxResult.intValue() == 1){
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);//开始关闭订单
        } else {
            //为获取到锁，继续判断，判断时间戳，看是否可以重置并获到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            //判断锁的是否存在，且当前时间大于锁的时间
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){

                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
                if(getSetResult == null || (getSetResult != null & StringUtils.equals(lockValueStr,getSetResult))){
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);//开始关闭订单
                } else {
                    log.info("没有获得分布式锁：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获得分布式锁：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }


        log.info("关闭订单任务结束");
    }

    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50);//有效期50秒防止死锁
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
//        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
    }
}
