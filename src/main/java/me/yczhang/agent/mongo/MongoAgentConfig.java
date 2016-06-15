package me.yczhang.agent.mongo;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.*;

/**
 * Created by YCZhang on 9/25/15.
 */
public class MongoAgentConfig {

	protected String host;
	protected Integer port;
	protected List<MongoCredential> credentials = new LinkedList<>();
	protected MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();

	public MongoAgentConfig() {
	}

	public MongoAgentConfig host(String host) {
		this.host = host;
		return this;
	}

	public MongoAgentConfig port(int port) {
		this.port = port;
		return this;
	}

	public MongoAgentConfig credential(String db, String user, String pass) {
		this.credentials.add(MongoCredential.createCredential(user, db, pass.toCharArray()));
		return this;
	}

	protected ServerAddress serverAddress() {
		if (this.host == null && this.port == null)
			return new ServerAddress();
		if (this.port == null)
			return new ServerAddress(this.host);
		return new ServerAddress(this.host, this.port);
	}

	protected MongoClientOptions options() {
		return optionsBuilder.build();
	}

	public static MongoAgentConfig parseMap(Map<String, String> props) {
		return parseMap(props, "");
	}

	public static MongoAgentConfig parseMap(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		final String PREFIX_CREDENTIAL = prefix + "Credentials.";
		final String SUFFIX_USER = ".User";
		final String SUFFIX_PASS = ".Pass";

		MongoAgentConfig config = new MongoAgentConfig();

		String tmp = null;

		tmp = props.get(prefix + "Host");
		if (tmp != null) config.host(tmp);

		tmp = props.get(prefix + "Port");
		if (tmp != null) config.port(Integer.parseInt(tmp));

		for (String key : props.keySet()) {
			if (key.startsWith(PREFIX_CREDENTIAL) && key.endsWith(SUFFIX_USER)) {
				String db = key.substring(PREFIX_CREDENTIAL.length(), key.length() - SUFFIX_USER.length());
				String user = props.get(key);
				String pass = props.get(PREFIX_CREDENTIAL + db + SUFFIX_PASS);

				config.credential(db, user, pass);
			}
		}

		return config;
	}

	public static Map<String, MongoAgentConfig> parseMaps(Map<String, String> props) {
		return parseMaps(props, "");
	}

	public static Map<String, MongoAgentConfig> parseMaps(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (!prefix.isEmpty())
			prefix += ".";

		final String PREFIX_MONGOS = prefix + "Mongos";

		Set<String> keys = new HashSet<>();
		for (String key : props.keySet()) {
			if (key.startsWith(PREFIX_MONGOS)) {
				String[] fields = key.substring(PREFIX_MONGOS.length()).split("\\.");
				if (fields.length < 2)
					continue;
				keys.add(fields[0]);
			}
		}

		Map<String, MongoAgentConfig> configs = new HashMap<>();
		for (String key : keys) {
			MongoAgentConfig config = parseMap(props, PREFIX_MONGOS + key);
			if (config != null)
				configs.put(key, config);
		}

		return configs;
	}
}
