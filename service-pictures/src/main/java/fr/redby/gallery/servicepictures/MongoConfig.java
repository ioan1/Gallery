package fr.redby.gallery.servicepictures;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackages = "fr.redby.gallery.servicepictures")
public class MongoConfig extends AbstractMongoConfiguration {

    private static final String HOST = "192.168.0.63";
    private static final int PORT = 27017;

    @Override
    protected String getDatabaseName() {
        return "service-pictures";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(
                new ServerAddress(HOST, PORT),
                MongoCredential.createCredential("root", "admin", "password".toCharArray()),
                MongoClientOptions.builder().build());
    }

}
