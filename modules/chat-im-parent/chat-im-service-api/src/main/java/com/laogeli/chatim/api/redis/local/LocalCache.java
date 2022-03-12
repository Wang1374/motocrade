package com.laogeli.chatim.api.redis.local;

import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * description: hongpei-work com.xunke.chat.redis.local
 *
 * @author HongPei
 * @date 2021/6/8 13:43
 */
@NoArgsConstructor
public class LocalCache {

    private volatile static LocalCache instance;

    /**
     * DCL方式获取单例
     *
     * @return
     */
    public static LocalCache getInstance() {
        if (instance == null) {
            synchronized (LocalCache.class) {
                if (instance == null) {
                    instance = new LocalCache();
                }
            }
        }
        return instance;
    }

    private ConcurrentHashMap<String, Node> cache = new ConcurrentHashMap<>(1024);

    public void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        if (ttl != -1) {
            if (timeUnit.equals(TimeUnit.SECONDS)) {
                ttl = ttl;
            }

            if (timeUnit.equals(TimeUnit.MINUTES)) {
                ttl = ttl * 60;
            }
            if (timeUnit.equals(TimeUnit.HOURS)) {
                ttl = ttl * 60 * 60;
            }
        }
        set(key, value, ttl);
    }

    public void set(String key, Object value, long ttl) {
        /*Assert.isTrue(StringUtils.hasLength(key), "key can't be empty");
        // Assert.isTrue(ttl > 0, "ttl must greater than 0");
        if (ttl == -1) {
            ttl = System.currentTimeMillis() + 3600 * 1000 * 365;
        } else {
            ttl = System.currentTimeMillis() + ttl;
        }
        long expireTime = System.currentTimeMillis() + ttl;
        Node newNode = new Node(key, value, expireTime);
        cache.put(key, newNode);*/
    }

    /**
     * 拿到的数据可能是已经过期的数据，可以再次判断一下
     * if（n.expireTime<System.currentTimeMillis()）{
     * return null;
     * }
     * 也可以直接返回整个节点Node ，交给调用者去取舍
     * <p>
     * <p>
     * 无法判断不存在该key,还是该key存的是一个null值，如果需要区分这两种情况
     * 可以定义一个全局标识，标识key不存在
     * public static final NOT_EXIST = new Object();
     * 返回值时
     * return n==null?NOT_EXIST:n.value;
     */
    public Object get(String key) {
        // Node n = cache.get(key);
        // return n == null ? null : n.value;
        return null;
    }

    /**
     * 删出KEY，并返回该key对应的数据
     */
    public Object remove(String key) {
        // return cache.remove(key);
        return null;
    }

    /**
     * 删除已经过期的数据
     */
    private class SwapExpiredNodeWork implements Runnable {
        @Override
        public void run() {
           /* long now = System.currentTimeMillis();
            Set<Map.Entry<String, Node>> entries = cache.entrySet();
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, Node> entry : entries) {
                Node node = entry.getValue();
                if (node.expireTime < now) {
                    keys.add(entry.getKey());
                }

            }
            keys.stream().forEach(val -> cache.remove(val));*/
        }
    }

    private static class Node implements Comparable<Node> {

        private String key;

        private Object value;

        private long expireTime;

        public Node(String key, Object value, long expireTime) {
            this.value = value;
            this.key = key;
            this.expireTime = expireTime;
        }

        /**
         * @see SwapExpiredNodeWork
         */
        @Override
        public int compareTo(Node o) {
            long r = this.expireTime - o.expireTime;
            if (r > 0) {
                return 1;
            }
            if (r < 0) {
                return -1;
            }
            return 0;
        }

    }


}
