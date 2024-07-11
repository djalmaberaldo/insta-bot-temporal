package com.instagram.bot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Component;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class MongoConnection implements AutoCloseable {

    private final MongoClient mongoClient;

    public MongoConnection() {
        this.mongoClient = setup();
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    private MongoClient setup() {
        String uri = "mongodb://localhost:27017";
        ConnectionString connectionString = new ConnectionString(uri);

        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings);
    }

    public MongoDatabase getMongoDatabase(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }
}
