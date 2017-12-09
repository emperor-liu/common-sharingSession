/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache.redis
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache.redis;

import java.util.List;
import java.util.regex.Pattern;

import redis.clients.jedis.JedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/** 
 * ClassName: JedisPoolSharded.java <br>
 * Description: 连接池分片<br>
 * Create by: name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * Create Time: 2017年4月8日<br>
 */
public class JedisPoolSharded extends Sharded<JedisPool, JedisPoolShardInfo> {

	public JedisPoolSharded(List<JedisPoolShardInfo> shards, Hashing algo, Pattern tagPattern) {
		super(shards, algo, tagPattern);
	}

	public JedisPoolSharded(List<JedisPoolShardInfo> shards, Hashing algo) {
		super(shards, algo);
	}

	public JedisPoolSharded(List<JedisPoolShardInfo> shards, Pattern tagPattern) {
		super(shards, tagPattern);
	}

	public JedisPoolSharded(List<JedisPoolShardInfo> shards) {
		super(shards);
	}

}
