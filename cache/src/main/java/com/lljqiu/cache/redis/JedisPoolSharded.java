package com.lljqiu.cache.redis;

import java.util.List;
import java.util.regex.Pattern;

import redis.clients.jedis.JedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

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
