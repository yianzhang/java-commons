package me.yczhang.agent.redis;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by YCZhang on 9/25/15.
 */
public class RedisAgentConfig {

	protected String host;
	protected Integer port;
	protected String pass;
	protected Integer timeout_ms;
	protected Integer defDB;
	protected JedisPoolConfig poolConfig = new JedisPoolConfig();

	public RedisAgentConfig() {
	}

	public RedisAgentConfig host(String host) {
		this.host = host;
		return this;
	}

	public RedisAgentConfig port(int port) {
		this.port = port;
		return this;
	}

	public RedisAgentConfig pass(String pass) {
		this.pass = pass;
		return this;
	}

	public RedisAgentConfig timeout_ms(int timeout_ms) {
		this.timeout_ms = timeout_ms;
		return this;
	}

	public RedisAgentConfig defDB(int defDB) {
		this.defDB = defDB;
		return this;
	}

	protected String host() {
		if (this.host != null)
			return this.host;
		else
			return Protocol.DEFAULT_HOST;
	}

	protected int port() {
		if (this.port != null)
			return this.port;
		else
			return Protocol.DEFAULT_PORT;
	}

	protected int timeout_ms() {
		if (this.timeout_ms != null)
			return this.timeout_ms;
		else
			return Protocol.DEFAULT_TIMEOUT;
	}

	protected int defDB() {
		if (this.defDB != null)
			return this.defDB;
		else
			return Protocol.DEFAULT_DATABASE;
	}

	public static RedisAgentConfig parseMap(Map<String, String> props) {
		return parseMap(props, "");
	}

	public static RedisAgentConfig parseMap(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		RedisAgentConfig config = new RedisAgentConfig();

		String tmp = null;

		tmp = props.get(prefix + "Host");
		if (tmp != null) config.host(tmp);

		tmp = props.get(prefix + "Port");
		if (tmp != null) config.port(Integer.parseInt(tmp));

		tmp = props.get(prefix + "Pass");
		if (tmp != null) config.pass(tmp);

		tmp = props.get(prefix + "Timeout.ms");
		if (tmp != null) config.timeout_ms(Integer.parseInt(tmp));

		tmp = props.get(prefix + "DefaultDB");
		if (tmp != null) config.defDB(Integer.parseInt(tmp));

		return config;
	}

	public static Map<String, RedisAgentConfig> parseMaps(Map<String, String> props) {
		return parseMaps(props, "");
	}

	public static Map<String, RedisAgentConfig> parseMaps(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (!prefix.isEmpty())
			prefix += ".";

		final String PREFIX_REDISS = prefix + "Rediss.";

		Set<String> keys = new HashSet<>();
		for (String key : props.keySet()) {
			if (key.startsWith(PREFIX_REDISS)) {
				String[] fields = key.substring(PREFIX_REDISS.length()).split("\\.");
				if (fields.length < 2)
					continue;
				keys.add(fields[0]);
			}
		}

		Map<String, RedisAgentConfig> configs = new HashMap<>();
		for (String key : keys) {
			RedisAgentConfig config = parseMap(props, PREFIX_REDISS + key);
			if (config != null)
				configs.put(key, config);
		}

		return configs;
	}
}
