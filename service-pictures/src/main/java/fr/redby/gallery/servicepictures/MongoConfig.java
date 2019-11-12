package fr.redby.gallery.servicepictures;

@Configuration
@EnableMongoRepositories(basePackages = "fr.redby.gallery.servicepictures")
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "gallery";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient("mongo", 27017);
    }

    @Override
    protected String getMappingBasePackage() {
        return "fr.redby.gallery.servicepictures";
    }
}