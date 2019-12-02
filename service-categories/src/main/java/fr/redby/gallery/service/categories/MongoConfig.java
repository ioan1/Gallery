package fr.redby.gallery.service.categories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "fr.redby.gallery.service.categories")
public class MongoConfig extends AbstractMongoConfiguration {

    private static final String HOST = "mongo";
    private static final int PORT = 27017;

    @Override
    protected String getDatabaseName() {
        return "service-categories";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(
                new ServerAddress(HOST, PORT),
                MongoCredential.createCredential("root", "admin", "password".toCharArray()),
                MongoClientOptions.builder().build());
    }

}
