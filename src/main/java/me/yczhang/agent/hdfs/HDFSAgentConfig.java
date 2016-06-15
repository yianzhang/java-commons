package me.yczhang.agent.hdfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by YCZhang on 9/28/15.
 */
public class HDFSAgentConfig {

	protected String namenode;
	protected String user;

	public HDFSAgentConfig() {

	}

	public HDFSAgentConfig namenode(String namenode) {
		this.namenode = namenode;
		return this;
	}

	public HDFSAgentConfig user(String user) {
		this.user = user;
		return this;
	}

	public static HDFSAgentConfig parseMap(Map<String, String> props) {
		return parseMap(props, "");
	}

	public static HDFSAgentConfig parseMap(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		HDFSAgentConfig config = new HDFSAgentConfig();

		String tmp = null;

		tmp = props.get(prefix + "Namenode");
		if (tmp != null) config.namenode(tmp);

		tmp = props.get(prefix + "User");
		if (tmp != null) config.user(tmp);

		return config;
	}

	public static Map<String, HDFSAgentConfig> parseMaps(Map<String, String> props) {
		return parseMaps(props, "");
	}

	public static Map<String, HDFSAgentConfig> parseMaps(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (!prefix.isEmpty())
			prefix += ".";

		final String PREFIX_HDFSS = prefix + "HDFSs.";

		Set<String> keys = new HashSet<>();
		for (String key : props.keySet()) {
			if (key.startsWith(PREFIX_HDFSS)) {
				String[] fields = key.substring(PREFIX_HDFSS.length()).split("\\.");
				if (fields.length < 2)
					continue;
				keys.add(fields[0]);
			}
		}

		Map<String, HDFSAgentConfig> configs = new HashMap<>();
		for (String key : keys) {
			HDFSAgentConfig config = parseMap(props, PREFIX_HDFSS + key);
			if (config != null)
				configs.put(key, config);
		}

		return configs;
	}

}
