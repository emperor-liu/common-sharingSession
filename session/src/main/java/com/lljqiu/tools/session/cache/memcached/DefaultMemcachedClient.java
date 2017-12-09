/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache.memcached
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache.memcached;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lljqiu.tools.session.cache.AbstractCacheClient;
import com.lljqiu.tools.session.cache.HQCacheException;
import com.lljqiu.tools.session.cache.PersistenceHolder;

/** 
 * ClassName: DefaultMemcachedClient.java <br>
 * Description: 
 * <pre>
 * 默认memcached缓存客户端实现
 * 
 * 本实现依赖spymemcached代码
 * 
 * <href>https://code.google.com/p/spymemcached/</href>
 * </pre>
 * Create by: name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * Create Time: 2017年4月8日<br>
 */
public class DefaultMemcachedClient<T extends Serializable> extends AbstractCacheClient implements
        MemcachedClient<T> {
    private PersistenceHolder                             persistenceHolder;
    private static Logger                                 log     = LoggerFactory
                                                                          .getLogger(DefaultMemcachedClient.class);
    private volatile boolean                              isInitd = false;
    private ObjectPool<net.spy.memcached.MemcachedClient> pool;

    /**
     * 初始化memcached客户端，在启动的时候如果初始化失败则启动失败
     */
    public void init() {
        if (!isInitd) {
            try {
                log.info("the memcached client will start ...");
                GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                config.setMinIdle(5);
                config.setMaxIdle(20);
                config.setMaxTotal(20);
                config.setMaxWaitMillis(5000);
                config.setTestOnBorrow(false);
                config.setTestOnCreate(false);
                config.setTestOnReturn(true);
                config.setTestWhileIdle(true);
                config.setNumTestsPerEvictionRun(1);
                config.setJmxEnabled(false);
                config.setSoftMinEvictableIdleTimeMillis(-1);//代表空闲时间
                config.setTimeBetweenEvictionRunsMillis(30000);//代表回收周期
                MemcachedClientFactory factory = new MemcachedClientFactory();
                pool = new GenericObjectPool<net.spy.memcached.MemcachedClient>(factory, config);
                log.info("start the memcached client success !");
            } catch (Exception e) {
                log.error("start the memcached client failed:", e);
                throw new RuntimeException("start the memcached client failed:", e);
            }
            isInitd = true;
        }
    }

    private net.spy.memcached.MemcachedClient getClient() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.error("get memcached client failed:" + e);
            return null;
        }
    }

    private void returnClient(net.spy.memcached.MemcachedClient client) {
        try {
            pool.returnObject(client);
        } catch (Exception e) {
            log.error("return memcached client failed:" + e);

        }
    }

    public void destroy() {
        if (isInitd) {
            log.info("the memcached client will stop ...");
            pool.close();
            log.info("the memcached client stoped ...");
            isInitd = false;
        }
    }

    public void put(String key, Serializable val) {
        int expiredTime = 0;
        if (persistenceHolder != null && persistenceHolder.getTypeExpiredMap() != null) {
            expiredTime = persistenceHolder.getTypeExpiredMap().get(val.getClass().getName());
        }

        net.spy.memcached.MemcachedClient client = getClient();
        try {
            client.set(key, expiredTime, val);
        } catch (Throwable e) {
            throw new HQCacheException("put Cache item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public void put(String key, Serializable val, int expiredTime) {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return;
        }
        try {
            client.set(key, expiredTime, val);
        } catch (Throwable e) {
            throw new HQCacheException("put Cache item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public Future<Object> asyncGet(String key) throws HQCacheException {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return null;
        }
        try {
            return client.asyncGet(key);
        } catch (Throwable e) {
            throw new HQCacheException("asyncGet item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(String key) {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return null;
        }
        try {
            Object object = client.get(key);
            if (object == null) {
                return null;
            }
            return (T) object;
        } catch (Throwable e) {
            throw new HQCacheException("get Cache item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public void delete(String key) {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return;
        }
        try {
            client.delete(key);
        } catch (Throwable e) {
            throw new HQCacheException("delete Cache item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public void incr(String key, int by, int expiredTime) {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return;
        }
        try {
            client.incr(key, by, expiredTime);
        } catch (Throwable e) {
            throw new HQCacheException("put Cache item exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public void incr(String key, int expiredTime) {
        net.spy.memcached.MemcachedClient client = getClient();
        if (client == null) {
            return;
        }
        try {
            client.incr(key, 1, expiredTime);
        } catch (Throwable e) {
            throw new HQCacheException("incr Cache exception.", e);
        } finally {
            returnClient(client);
        }
    }

    public void setPersistenceHolder(PersistenceHolder persistenceHolder) {
        this.persistenceHolder = persistenceHolder;
    }

    private class MemcachedClientFactory extends
            BasePooledObjectFactory<net.spy.memcached.MemcachedClient> {

        public MemcachedClientFactory() {
            super();
        }

        @Override
        public net.spy.memcached.MemcachedClient create() throws Exception {
            List<InetSocketAddress> serverList = getAddressWithString();
            return new net.spy.memcached.MemcachedClient(serverList);
        }

        @Override
        public PooledObject<net.spy.memcached.MemcachedClient> wrap(net.spy.memcached.MemcachedClient client) {
            return new DefaultPooledObject<net.spy.memcached.MemcachedClient>(client);
        }

        @Override
        public boolean validateObject(PooledObject<net.spy.memcached.MemcachedClient> client) {
            return client.getObject().getAvailableServers().size() > 0;
        }

        @Override
        public void destroyObject(PooledObject<net.spy.memcached.MemcachedClient> p)
                throws Exception {
            p.getObject().shutdown();
        }
    }
}
