package me.yczhang.agent.hdfs;

import org.apache.hadoop.fs.FileSystem;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YCZhang on 9/29/15.
 */
public class HDFSAgentManager implements Closeable {

	protected Map<String, HDFSAgent> agents = new ConcurrentHashMap<>();

	public HDFSAgentManager(Map<String, HDFSAgentConfig> configs) throws IOException, InterruptedException {
		for (Map.Entry<String, HDFSAgentConfig> entry : configs.entrySet()) {
			agents.put(entry.getKey(), new HDFSAgent(entry.getValue()));
		}
	}

	public HDFSAgent getAgent(String name) {
		return agents.get(name);
	}

	public FileSystem getFileSystemOf(String name) {
		return agents.containsKey(name) ? agents.get(name).getFileSystem() : null;
	}

	@Override
	public void close() throws IOException {
		for (HDFSAgent agent : agents.values()) {
			agent.close();
		}
	}
}
