package me.yczhang.agent.redis;

import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YCZhang on 9/29/15.
 */
public class RedisAgentManager implements Closeable {

	protected Map<String, RedisAgent> agents = new ConcurrentHashMap<>();

	public RedisAgentManager(Map<String, RedisAgentConfig> configs) {
		for (Map.Entry<String, RedisAgentConfig> entry : configs.entrySet()) {
			agents.put(entry.getKey(), new RedisAgent(entry.getValue()));
		}
	}

	public RedisAgent getAgent(String name) {
		return agents.get(name);
	}

	public Jedis getJedis(String name) {
		return agents.containsKey(name) ? getAgent(name).getJedis() : null;
	}

	public Jedis getJedis(String name, int db) {
		return agents.containsKey(name) ? getAgent(name).getJedis(db) : null;
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

	@Override
	public void close() throws IOException {
		for (RedisAgent agent : agents.values()) {
			agent.close();
		}
	}
}
