package com.laogeli.common.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : yannan
 * @date : 2019/12/12 11:08
 */
@AllArgsConstructor
@Slf4j
@Service
public class RedisService {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;


  /**
   * 存储数据
   */
  public void set(String key, String value) {
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
    ops.set(key, value);

  }


  /**
   * 获取数据
   */
  public String get(String key) {
    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
    return ops.get(key);
  }


  /**
   * 设置超期时间
   */
  public boolean expire(String key, long expire) {
    Boolean expire2 = stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    return expire2;
  }


  /**
   * 删除数据
   */
  public void remove(String key) {
    stringRedisTemplate.delete(key);
  }


  /**
   * 自增操作
   *
   * @param delta 自增步长
   */
  public Long increment(String key, long delta) {
    return stringRedisTemplate.opsForValue().increment(key, delta);
  }
}
