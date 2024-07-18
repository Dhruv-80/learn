package com.example.mongotest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MongoEmbeddedTest {

	public static void main(String[] args) {
		// Connect to MongoDB
		String uri = "mongodb://localhost:27017";
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			MongoDatabase database = mongoClient.getDatabase("testdb");
			MongoCollection<Document> collection = database.getCollection("auditCollectionName");

			// Drop the collection if it exists
			collection.drop();

			// Insert test data
			Document document = new Document("_id", "container123")
					.append("events", Arrays.asList(
							new Document("time", "2024-06-01T10:00:00Z").append("event", "Event1"),
							new Document("time", "2024-06-02T11:00:00Z").append("event", "Event2"),
							new Document("time", "2024-06-03T12:00:00Z").append("event", "Event3"),
							new Document("time", "2024-06-04T13:00:00Z").append("event", "Event4"),
							new Document("time", "2024-06-05T14:00:00Z").append("event", "Event5"),
							new Document("time", "2024-06-06T15:00:00Z").append("event", "Event6")
					));
			collection.insertOne(document);

			// Perform the aggregation
			List<Document> pipeline = Arrays.asList(
					new Document("$match", new Document("_id", "container123")),
					new Document("$unwind", "$events"),
					new Document("$sort", new Document("events.time", -1)),
					new Document("$group", new Document("_id", "$_id")
							.append("sortedEvents", new Document("$push", "$events"))),
					new Document("$project", new Document("events", new Document("$slice", Arrays.asList("$sortedEvents", 3, 3))))
			);

			List<Document> results = collection.aggregate(pipeline).into(new ArrayList<>());

			// Deserialize the result using Gson
			Gson gson = new Gson();
			Type type = new TypeToken<ContainerAuditModel<Map<String, Object>>>() {}.getType();
			ContainerAuditModel<Map<String, Object>> result = gson.fromJson(gson.toJson(results.get(0)), type);

			// Print the result
			System.out.println(result);
		}
	}

	static class ContainerAuditModel<T> {
		private List<T> events;

		public List<T> getEvents() {
			return events;
		}

		public void setEvents(List<T> events) {
			this.events = events;
		}

		@Override
		public String toString() {
			return "ContainerAuditModel{" +
					"events=" + events +
					'}';
		}
	}
}
