package me.yczhang.agent.ehcache;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YCZhang on 9/28/15.
 */
public class EhcacheAgentConfig {

	protected DiskStoreConfiguration diskStoreConfig;
	protected Map<String, CacheConfiguration> cacheConfigs = new HashMap<>();

	public EhcacheAgentConfig() {
	}

	public EhcacheAgentConfig diskStore(String diskStore_path) {
		if (diskStore_path == null)
			this.diskStoreConfig = null;
		else
			this.diskStoreConfig = new DiskStoreConfiguration().path(diskStore_path);
		return this;
	}

	public EhcacheAgentConfig cache(CacheConfiguration config) {
		this.cacheConfigs.put(config.getName(), config);
		return this;
	}

	public EhcacheAgentConfig cache(String name, int maxEntriesLocalHeap) {
		this.cacheConfigs.put(name, new CacheConfiguration(name, maxEntriesLocalHeap));
		return this;
	}

	public EhcacheAgentConfig cache_overflowToDisk(String name) {
		CacheConfiguration config = this.cacheConfigs.get(name);
		if (config != null) {
			config.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP));
		}
		return this;
	}

	public EhcacheAgentConfig cache_eternal(String name) {
		CacheConfiguration config = this.cacheConfigs.get(name);
		if (config != null) {
			config.eternal(true);
		}
		return this;
	}

	public EhcacheAgentConfig cache_timeToLiveSeconds(String name, int timeToLiveSeconds) {
		CacheConfiguration config = this.cacheConfigs.get(name);
		if (config != null) {
			config.timeToLiveSeconds(timeToLiveSeconds);
		}
		return this;
	}

	public EhcacheAgentConfig cache_timeToIdleSeconds(String name, int timeToIdleSeconds) {
		CacheConfiguration config = this.cacheConfigs.get(name);
		if (config != null) {
			config.timeToIdleSeconds(timeToIdleSeconds);
		}
		return this;
	}

	protected DiskStoreConfiguration diskStoreConfig() {
		return this.diskStoreConfig;
	}

	public static EhcacheAgentConfig parseMap(Map<String, String> props) {
		return parseMap(props, "");
	}

	public static EhcacheAgentConfig parseMap(Map<String, String> props, String prefix) {
		if (prefix == null)
			prefix = "";
		else if (prefix.length() > 0)
			prefix = prefix + ".";

		final String PREFIX_CACHE = prefix + "Caches.";
		final String SUFFIX_MAXENTRIESLOCALHEAP = ".MaxEntriesLocalHeap";
		final String SUFFIX_OVERFLOWTODISK = ".OverFlowToDisk";
		final String SUFFIX_ETERNAL = ".Eternal";
		final String SUFFIX_TIMETOLIVE_SEC = ".TimeToLive.sec";
		final String SUFFIX_TIMETOIDLE_SEC = ".TimeToIdle.sec";


		EhcacheAgentConfig config = new EhcacheAgentConfig();

		String tmp = null;

		tmp = props.get(prefix + "DiskStore.Path");
		if (tmp != null) config.diskStore(tmp);

		for (Map.Entry<String, String> entry : props.entrySet()) {
			String key = entry.getKey();

			if (key.startsWith(PREFIX_CACHE) && key.endsWith(SUFFIX_MAXENTRIESLOCALHEAP)) {
				String name = key.substring(PREFIX_CACHE.length(), key.length() - SUFFIX_MAXENTRIESLOCALHEAP.length());

				int maxEntriesLocalHeap = Integer.parseInt(entry.getValue());
				config.cache(name, maxEntriesLocalHeap);

				tmp = props.get(PREFIX_CACHE + name + SUFFIX_OVERFLOWTODISK);
				if (tmp != null && Boolean.parseBoolean(tmp)) config.cache_overflowToDisk(name);

				tmp = props.get(PREFIX_CACHE + name + SUFFIX_ETERNAL);
				if (tmp != null && Boolean.parseBoolean(tmp)) config.cache_eternal(name);

				tmp = props.get(PREFIX_CACHE + name + SUFFIX_TIMETOLIVE_SEC);
				if (tmp != null) config.cache_timeToLiveSeconds(name, Integer.parseInt(tmp));

				tmp = props.get(PREFIX_CACHE + name + SUFFIX_TIMETOIDLE_SEC);
				if (tmp != null) config.cache_timeToIdleSeconds(name, Integer.parseInt(tmp));
			}
		}

		return config;
	}
}
