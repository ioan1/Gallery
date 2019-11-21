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

    @Override
    protected String getDatabaseName() {
        return "service-albums";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(
                new ServerAddress("192.168.0.63", 27017),
                MongoCredential.createCredential("root", "admin", "password".toCharArray()),
                MongoClientOptions.builder().build());
    }

}
