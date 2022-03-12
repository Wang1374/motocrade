package com.laogeli.chatim.api.redis.service;

import io.swagger.models.auth.In;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * description: hongpei-work com.xunke.chat.redis
 *
 * @author HongPei
 * @date 2021/6/8 13:38
 */
public interface RedisCacheService<T> {

    T get(String key);

    List getLike(String key);

    T get(String key, Boolean isNeedLocal);

    String getString(String key);


    Integer incr(String key, Integer count);

    Integer descr(String key, Integer count);

    Integer incr(String key, Integer count, long expireTime);

    Integer descr(String key, Integer count, long expireTime);

    Double incr(String key, Double count);

    Double descr(String key, Double count);

    void put(String key, T t, long expireTime, Boolean isNeedLocal);

    void put(String key, T t, long expireTime);

    void put(String key, T t, long expireTime, TimeUnit timeUnit);

    void put(String key, T t, long expireTime, TimeUnit timeUnit, Boolean isNeedLocal);

    Long getKeyExpireTime(String key, TimeUnit timeUnit);

    void put(String key, T t, Boolean isNeedLocal);

    void put(String key, T t);

    void putNotChangeExpireTime(String key, T t);

    void putNeedRandom(String key, String t, boolean isNeed);

    void put(String key, String t);

    void remove(String key, Boolean isNeedLocal);

    void remove(String key);

    void likeRemove(String key);

    void removeAll(List<String> keys);

    boolean isContain(String key);

    boolean isContain(String key, Boolean isNeedLocal);

    boolean setIfAbsent(String key, String value, long seconds);

    boolean isSetMember(String key, String value);

    void setAdd(String key, String value);

    void setAdd(String key, String value, long expireTime);

    long getSetSize(String key);

    Set getSet(String key);

    long setNX(String key, String value);

    public void listAdd(String key, T value);

    void listAdd(String key, T value, long expireTime, TimeUnit timeUnit);

    public void listAdd(String key, List<T> value);

    void listAdd(String key, List<T> values, long expireTime, TimeUnit timeUnit);

    void alterExpireTime(String key, long expireTime, TimeUnit timeUnit);

    public long getListSize(String key);

    public long listRemove(String key, Object value, long count);

    public List<T> getList(String key);

    public Boolean hasKey(String key);

    public void publish(String key, String dto);

    String deserializeByte(byte[] key);

    public Boolean isSetMemeber(String key, Object value);

    public void setAddString(String key, String value);

    public void setRemoveAValue(String key, String value);

    boolean addMap(String key, Map map);

    T hashGet(String key, String field);

    boolean hashSet(String key, String field, Object value);

    boolean addMap(String key, Map map, long expireTime);

    Map getMap(String key);

    public boolean updateAValueInMap(String key, String hashKey, Object hashValue);

    public void setKeyExpireSeconds(String key, long seconds);

    public List multiGet(Collection<String> list);

    void incr(String s);

    void zset(String lisetName,String key,long seconds);

    Set<Object> rangeZset(String key,long start,long end);

    Set<String> isKey(String key);

    Integer rank(String key, Object object);

    Set<Object> rangeRange(String key,int start,int end);

    int zrem(String key,String value);


    Long zremrangebyscore(String key,double min,double max);


}
