package fr.redby.gallery.service.statistics.repositories;

import fr.redby.gallery.service.statistics.beans.SizePerYear;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SizePerYearRepository extends MongoRepository<SizePerYear, Integer> {
    //
    int STAT_IDENTIFIER = 1;

}
