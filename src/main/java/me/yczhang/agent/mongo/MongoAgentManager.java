package me.yczhang.agent.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YCZhang on 9/29/15.
 */
public class MongoAgentManager implements Closeable {

	protected Map<String, MongoAgent> agents = new ConcurrentHashMap<>();

	public MongoAgentManager(Map<String, MongoAgentConfig> configs) {
		for (Map.Entry<String, MongoAgentConfig> entry : configs.entrySet()) {
			agents.put(entry.getKey(), new MongoAgent(entry.getValue()));
		}
	}

	public MongoAgent getAgent(String name) {
		return agents.get(name);
	}

	public MongoDatabase getDataBase(String name, String db) {
		return agents.containsKey(name) ? agents.get(name).getDataBase(db) : null;
	}

	public MongoCollection<Document> getCollection(String name, String db, String collection) {
		MongoDatabase database = getDataBase(name, db);
		return database == null ? null : database.getCollection(collection);
	}

	public <T> MongoCollection<T> getCollection(String name, String db, String collection, Class<T> klass) {
		MongoDatabase database = getDataBase(name, db);
		return database == null ? null : database.getCollection(collection, klass);
	}

	@Override
	public void close() throws IOException {
		for (MongoAgent agent : agents.values()) {
			agent.close();
		}
	}
}
