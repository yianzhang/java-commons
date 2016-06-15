package me.yczhang.agent.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by YCZhang on 9/25/15.
 */
public class RedisAgent implements Closeable {

	protected JedisPool pool;

	public RedisAgent(RedisAgentConfig config) {
		this.pool = new JedisPool(config.poolConfig, config.host(), config.port(), config.timeout_ms(), config.pass, config.defDB());
	}

	@Override
	public void close() throws IOException {
		try {
			this.pool.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public Jedis getJedis() {
		return this.pool.getResource();
	}

	public Jedis getJedis(int db) {
		Jedis jedis = this.pool.getResource();
		jedis.select(db);
		return jedis;
	}

	@Deprecated
	public void returnJedis(Jedis jedis) {
		if (jedis != null) {
			try {
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
