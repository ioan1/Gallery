package fr.redby.gallery.service.albums;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "fr.redby.gallery.service.albums")
public class MongoConfig extends AbstractMongoConfiguration {

    public static final String HOST = "192.168.0.63";
    public static final int PORT = 27017;

    @Override
    protected String getDatabaseName() {
        return "service-albums";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(
                new ServerAddress(HOST, PORT),
                MongoCredential.createCredential("root", "admin", "password".toCharArray()),
                MongoClientOptions.builder().build());
    }

}
