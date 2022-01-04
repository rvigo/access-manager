package br.com.rvigo.accessmanager.configurations;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private String port;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s/%s", host, port, database));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(new BinaryToUUIDConverter(), new UUIDToBinaryConverter()));
    }
}
