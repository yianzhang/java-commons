package me.yczhang.agent.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by YCZhang on 9/25/15.
 */
public class MongoAgent implements Closeable {

	protected MongoClient client;

	public MongoAgent(MongoAgentConfig config) {
		this.client = new MongoClient(config.serverAddress(), config.credentials, config.options());
	}

	@Override
	public void close() throws IOException {
		this.client.close();
	}

	public MongoDatabase getDataBase(String db) {
		return this.client.getDatabase(db);
	}

	public MongoCollection<Document> getCollection(String db, String collection) {
		return this.client.getDatabase(db).getCollection(collection);
	}

	public <T> MongoCollection<T> getCollection(String db, String collection, Class<T> klass) {
		return this.client.getDatabase(db).getCollection(collection, klass);
	}
}
