package me.yczhang.agent.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.Closeable;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;

/**
 * Created by YCZhang on 9/28/15.
 */
public class HDFSAgent implements Closeable {

	protected FileSystem fileSystem;

	public HDFSAgent(final HDFSAgentConfig config) throws IOException, InterruptedException {
		this.fileSystem = UserGroupInformation.createRemoteUser(config.user).doAs(new PrivilegedExceptionAction<FileSystem>() {
			@Override
			public FileSystem run() throws Exception {
				Configuration conf = new Configuration();
				conf.set("fs.default.name", config.namenode);
				conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
				conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

				return FileSystem.get(conf);
			}
		});
	}

	public FileSystem getFileSystem() {
		return this.fileSystem;
	}

	@Override
	public void close() throws IOException {
		this.fileSystem.close();
	}
}
