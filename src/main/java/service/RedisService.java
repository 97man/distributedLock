package service;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 第一种分布式锁
 */
@Component
public class RedisService {

    private static  final Logger logger= LoggerFactory.getLogger(RedisService.class);
    @Autowired
    JedisPool jedisPool;

    //获取锁之前的超时时间
    private long acquireTimeOut=5000;
    //获取锁之后的释放时间，防止死锁的产生
    private int timeOut=10000;

    /**
     * 获取分布式锁
     * @param lockName 锁名称
     * @param val
     * @return 锁标识
     */
    public boolean getRedisLock(String lockName,String val){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            Long endTime=System.currentTimeMillis()+acquireTimeOut;
            if (System.currentTimeMillis()<endTime){
               if (jedis.setnx(lockName,val)==1){
                   jedis.expire(lockName,timeOut/1000);
                   return true;
               }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            returnResource(jedis);

        }
        return false;
    }
    public void unRedisLock(String lockName){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            jedis.del(lockName);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            returnResource(jedis);
        }
    }
    public String get(String key){
        Jedis jedis=null;
        String value=null;
        try {
            jedis=jedisPool.getResource();
            value=jedis.get(key);
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            returnResource(jedis);
        }
        return value;
    }
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }
    private void returnResource(Jedis jedis) {
        try{
            if (jedis!=null){
                jedis.close();
            }
        }catch (Exception e){

        }
    }
}