package com.laogeli.chatim.api.redis.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.laogeli.chatim.api.redis.local.LocalCache;
import com.laogeli.chatim.api.redis.service.RedisCacheService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description: hongpei-work com.xunke.chat.redis.service.impl
 *
 * @author HongPei
 * @date 2021/6/8 13:40
 */
@Service
public class RedisCacheServiceImp<T> implements RedisCacheService<T> {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImp.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private LocalCache localCache = LocalCache.getInstance();

    @Override
    public T get(String key) {
        return get(key, false);
    }

    @Override
    public List getLike(String key) {
        if (StringUtils.isNotBlank(key)) {
            Set<String> keys = redisTemplate.keys(key);
            if (CollectionUtils.isNotEmpty(keys)) {
                return multiGet(keys);
            }
        }
        return null;
    }


    @Override
    public T get(String key, Boolean isNeedLocal) {
        T value = null;
        try {
            if (isNeedLocal) {
//                value =  (T) localCache.get(key);
            }
            if (value != null) {
                return value;
            }
            if (key != null && redisTemplate != null) {
                T t = (T) redisTemplate.opsForValue().get(key);
                if (t != null) {
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString(String key) {
        if (StringUtils.isNotBlank(key)) {
            return stringRedisTemplate.opsForValue().get(key);
        }
        return null;
    }


    @Override
    public Integer incr(String key, Integer count) {

        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().incrBy(key.getBytes(), count).intValue();
        } else {
            redisTemplate.opsForValue().set(key, count);
            value = (Integer) redisTemplate.opsForValue().get(key);
        }
        return value;
    }

    @Override
    public Integer descr(String key, Integer count) {
        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().decrBy(key.getBytes(), count).intValue();
        } else {
            redisTemplate.opsForValue().set(key, count);
            value = (Integer) redisTemplate.opsForValue().get(key);
        }
        return value;
    }

    @Override
    public Integer incr(String key, Integer count, long expireTime) {
        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().incrBy(key.getBytes(), count).intValue();
        } else {
            redisTemplate.opsForValue().set(key, count, expireTime, TimeUnit.SECONDS);
            value = (Integer) redisTemplate.opsForValue().get(key);
        }
        return value;
    }

    @Override
    public Integer descr(String key, Integer count, long expireTime) {
        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().decrBy(key.getBytes(), count).intValue();
        } else {
            redisTemplate.opsForValue().set(key, count, expireTime, TimeUnit.SECONDS);
            value = (Integer) redisTemplate.opsForValue().get(key);
        }
        return value;
    }

    @Override
    public Double incr(String key, Double count) {
        Double value = Double.parseDouble(redisTemplate.opsForValue().get(key).toString());
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().incrBy(key.getBytes(), count).doubleValue();
        } else {
            redisTemplate.opsForValue().set(key, count);
            value = Double.parseDouble(redisTemplate.opsForValue().get(key).toString());
        }
        return value;
    }

    @Override
    public Double descr(String key, Double count) {
        Double value = Double.parseDouble(redisTemplate.opsForValue().get(key).toString());
        if (value != null) {
            value = redisTemplate.getConnectionFactory().getConnection().incrBy(key.getBytes(), -count).doubleValue();
        } else {
            redisTemplate.opsForValue().set(key, count);
            value = Double.parseDouble(redisTemplate.opsForValue().get(key).toString());
        }
        return value;
    }

    @Override
    public void put(String key, T t, long expireTime, Boolean isNeedLocal) {
        this.put(key, t, expireTime, TimeUnit.SECONDS, isNeedLocal);
    }

    @Override
    public void put(String key, T t, long expireTime) {
        this.put(key, t, expireTime, TimeUnit.SECONDS);
    }



    @Override
    public void put(String key, T t, long expireTime, TimeUnit timeUnit) {
        put(key, t, expireTime, timeUnit, false);
    }

    @Override
    public void put(String key, T t, long expireTime, TimeUnit timeUnit, Boolean isNeedLocal) {
        if (isNeedLocal) {
//            localCache.set(key,t,expireTime,timeUnit);
        }
        try {
            if (key != null && redisTemplate != null && expireTime != -1 && t != null) {
                redisTemplate.opsForValue().set(key, t, expireTime, timeUnit);
            } else {
                logger.error(key + redisTemplate);
                if (isContain(key)) {
                    Long restTime = redisTemplate.getExpire(key, timeUnit);
                    if (restTime != null && restTime > 0) {
                        redisTemplate.opsForValue().set(key, t, restTime, timeUnit);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public Long getKeyExpireTime(String key, TimeUnit timeUnit) {
        try {
            return redisTemplate.getExpire(key, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String key, T t, Boolean isNeedLocal) {
        //put(key, t, RandomUtils.nextInt(30, 180) * 60, TimeUnit.SECONDS, false);
        put(key, t, RandomUtils.nextInt(RandomUtil.getRandom(), 180) * 60, TimeUnit.SECONDS, false);

    }

    @Override
    public void put(String key, T t) {
        //this.put(key, t, RandomUtils.nextInt(30, 180) * 60);
        this.put(key, t, RandomUtils.nextInt(RandomUtil.getRandom(), 180) * 60);

    }

    @Override
    public void putNotChangeExpireTime(String key, T t) {
        this.put(key, t, -1, TimeUnit.SECONDS);
    }

    @Override
    public void putNeedRandom(String key, String t, boolean isNeed) {
        try {

            if (key != null && t != null && !key.trim().equals("") && !t.trim().equals("") && redisTemplate != null && isNeed) {
                Random rnd = new Random();
                redisTemplate.opsForValue().set(key, t, 60 * (60 + rnd.nextInt(5)), TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, t, 60 * 60, TimeUnit.SECONDS);
            }
            remove(String.format("lock%s", key));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void put(String key, String t) {
        this.putNeedRandom(key, t, true);
    }

    @Override
    public void remove(String key, Boolean isNeedLocal) {
        if (isNeedLocal) {
//            localCache.remove(key);
        }
        redisTemplate.delete(key);
    }

    @Override
    public void remove(String key) {
        remove(key, true);
    }

    @Override
    public void likeRemove(String key) {
        Set<String> keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }

    @Override
    public void removeAll(List<String> keys) {
        keys.stream().forEach(val -> remove(val));
    }

    @Override
    public boolean isContain(String key) {
        return this.isContain(key, false);
    }

    @Override
    public boolean isContain(String key, Boolean isNeedLocal) {
        boolean flag = false;
        if (isNeedLocal) {
//            flag = localCache.isContain(key);
        }
        if (flag == true) {
            return flag;
        }


        try {
            if (key != null && redisTemplate != null) {
                flag = redisTemplate.hasKey(key);

//                if(!key.contains("lock") && redisTemplate.hasKey(String.format("lock%s",key))){
//                    for(int i= 0; i<3;i++){
//                        Thread.sleep(1000 * 3);
//                        if (redisTemplate.hasKey(key)) {
//                            return true;
//                        }
//                    }
//                }
//                if(!key.contains("lock") && redisTemplate.opsForValue().setIfAbsent(String.format("lock%s",key),String.format("lock%s",key),1,TimeUnit.MINUTES)){
//                    return flag;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean setIfAbsent(String key, String value, long seconds) {
        try {
            boolean f = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (f) {
                stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isSetMember(String key, String value) {
        boolean flag = false;
        try {
            flag = redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {

        }
        return flag;
    }

    @Override
    public void setAdd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public void setAdd(String key, String value, long expireTime) {
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

    }

    @Override
    public long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Set getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public long setNX(String key, String value) {
        Boolean var = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (var && var != null) {
            redisTemplate.expire(key, 3, TimeUnit.SECONDS);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void listAdd(String key, T value) {
        listAdd(key, value, -1, null);
    }

    @Override
    public void listAdd(String key, T value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForList().leftPush(key, value);
        if (expireTime != -1 && timeUnit != null) {
            redisTemplate.expire(key, expireTime, timeUnit);
        }
    }

    @Override
    public void listAdd(String key, List values) {
        listAdd(key, values, -1, null);
    }

    @Override
    public void listAdd(String key, List values, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForList().leftPushAll(key, values);
        if (expireTime != -1 && timeUnit != null) {
            redisTemplate.expire(key, expireTime, timeUnit);
        }
    }

    @Override
    public void alterExpireTime(String key, long expireTime, TimeUnit timeUnit) {
        if (expireTime != -1 && timeUnit != null) {
            redisTemplate.expire(key, expireTime, timeUnit);
        }
    }

    @Override
    public long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public long listRemove(String key, Object value, long count) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public List<T> getList(String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public Boolean hasKey(String key) {
        Boolean result = null;
        try {
            if (key != null && redisTemplate != null) {
                result = redisTemplate.hasKey(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void publish(String key, String dto) {
        stringRedisTemplate.convertAndSend(key, dto);
    }

    @Override
    public String deserializeByte(byte[] body) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        return serializer.deserialize(body);
    }

    @Override
    public Boolean isSetMemeber(String key, Object value) {
        Boolean result = null;
        try {
            if (key != null && redisTemplate != null) {
                result = redisTemplate.opsForSet().isMember(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setAddString(String key, String value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }

    @Override
    public void setRemoveAValue(String key, String value) {
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    @Override
    public boolean addMap(String key, Map map) {
        if (StringUtils.isBlank(key) || map == null) {
            return false;
        } else {
            //if(redisTemplate.opsForHash().)
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }
    }

    @Override
    public T hashGet(String key, String field) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(field)) {
            return (T) redisTemplate.opsForHash().get(key, field);
        }
        return null;
    }

    @Override
    public boolean hashSet(String key, String field, Object value) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(field) && value != null) {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean addMap(String key, Map map, long expireTime) {
        if (StringUtils.isNotBlank(key) && map != null && expireTime > 0) {
            redisTemplate.opsForHash().putAll(key, map);
//            redisTemplate.getConnectionFactory().getConnection().hMSet(key.getBytes(),map);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public Map getMap(String key) {
        if (StringUtils.isNotBlank(key)) {
            Map map = redisTemplate.opsForHash().entries(key);
            if (map != null && !map.isEmpty()) {
                return map;
            }
        }
        return null;
    }

    @Override
    public boolean updateAValueInMap(String key, String hashKey, Object hashValue) {
        if (key == null) {
            return false;
        } else {
            return redisTemplate.opsForHash().putIfAbsent(key, hashKey, hashValue);
        }
    }

    @Override
    public void setKeyExpireSeconds(String key, long seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @Override
    public List multiGet(Collection<String> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return redisTemplate.opsForValue().multiGet(list);
        }
        return null;
    }

    @Override
    public void incr(String s) {

    }

    @Override
    public void zset(String listName,String key, long seconds) {
        redisTemplate.opsForZSet().add(listName,key,seconds);
    }

    @Override
    public Set<Object> rangeZset(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<String> isKey(String key) {
       return redisTemplate.keys("*"+"#"+key);
    }

    @Override
    public Integer rank(String key, Object object) {
        Long rank = redisTemplate.opsForZSet().rank(key, object);
        if(rank==null){
            return -1;
        }
        return rank.intValue();
    }

    @Override
    public Set<Object> rangeRange(String key, int start, int end) {
        Set<Object> range = redisTemplate.opsForZSet().range(key, start, end);
        return range;
    }

    @Override
    public int zrem(String key,String value) {
        Long remove = redisTemplate.opsForZSet().remove(key, value);
        return remove.intValue();
    }


    @Override
    public Long zremrangebyscore(String key,double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key,min,max);
    }


}

