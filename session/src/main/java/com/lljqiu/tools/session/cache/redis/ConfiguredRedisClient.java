/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache.redis
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache.redis;

import com.lljqiu.tools.session.cache.AbstractCacheClient;
import com.lljqiu.tools.session.cache.Assert;

import redis.clients.jedis.JedisPoolConfig;

/** 
 * ClassName: ConfiguredRedisClient.java <br>
 * Description: redis 参数配置<br>
 * Create by: name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * Create Time: 2017年4月8日<br>
 */
public abstract class ConfiguredRedisClient extends AbstractCacheClient {

	private static final String POOL_MAX_WAIT_MILLS = "poolMaxWaitMills";
	private static final String POOL_IDLE_SIZE = "poolIdleSize";
	private static final String POOL_MAX_SIZE = "poolMaxSize";
	private static final int MAX_TOTAL = 100;
	private static final long MAX_WAIT_MILLIS = 1000L * 10;
	private static final int MAX_IDLE = 1000 * 60;

	protected void checkConfig() {
		Assert.isNotNull("init error, cache server config not found.", this.getServer());
	}

	protected String[] buildConfig(String server) {
		Assert.isNotBlank("init error, cache server config error.", server);
		String[] serverInfo = server.split(":");
		Assert.isNotNull("init error, cache server config error.", serverInfo);
		return serverInfo;
	}

	protected JedisPoolConfig configRedisPool() {
		JedisPoolConfig redisConfig = new JedisPoolConfig();
		// Jedis池配置
		redisConfig.setMaxTotal(this.<Integer> getConfigVal(POOL_MAX_SIZE, MAX_TOTAL));
		redisConfig.setMaxIdle(this.<Integer> getConfigVal(POOL_IDLE_SIZE, MAX_IDLE));
		redisConfig.setMaxWaitMillis(this.<Long> getConfigVal(POOL_MAX_WAIT_MILLS, MAX_WAIT_MILLIS));
		
		// 可选配置
		String minIdle = this.getStrConfigVal("poolMinIdle", null);
		if (minIdle != null) {
			redisConfig.setMinIdle(Integer.parseInt(minIdle));
		}
		String testWhileIdle = this.getStrConfigVal("poolTestWhileIdle", null);
		if (testWhileIdle != null) {
			redisConfig.setTestWhileIdle(Boolean.valueOf(testWhileIdle));
		}
		String minEvictableIdleTimeMillis = this.getStrConfigVal("poolMinEvictableIdleTimeMillis", null);
		if (minEvictableIdleTimeMillis != null) {
			redisConfig.setMinEvictableIdleTimeMillis(Integer.parseInt(minEvictableIdleTimeMillis));
		}
		String timeBetweenEvictionRunsMillis = this.getStrConfigVal("poolTimeBetweenEvictionRunsMillis", null);
		if (timeBetweenEvictionRunsMillis != null) {
			redisConfig.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis));
		}
		String numTestsPerEvictionRun = this.getStrConfigVal("poolNumTestsPerEvictionRun", null);
		if (numTestsPerEvictionRun != null) {
			redisConfig.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));
		}
		return redisConfig;
	}

}
