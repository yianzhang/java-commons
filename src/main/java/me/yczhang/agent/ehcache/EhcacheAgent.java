package me.yczhang.agent.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by YCZhang on 9/28/15.
 */
public class EhcacheAgent implements Closeable {

	private Configuration cmConfig;
	private CacheManager cm;

	public EhcacheAgent(EhcacheAgentConfig config) {
		cmConfig = new Configuration();
		if (config.diskStoreConfig != null)
			cmConfig.diskStore(config.diskStoreConfig);
		for (CacheConfiguration cacheConfig : config.cacheConfigs.values())
			cmConfig.addCache(cacheConfig);

		this.cm = new CacheManager(cmConfig);
	}

	public Cache getCache(String name) {
		return this.cm.getCache(name);
	}

	public EhcacheAgent addCache(Cache cache) {
		this.cm.addCache(cache);
		return this;
	}

	@Override
	public void close() throws IOException {
		this.cm.shutdown();
	}
}
