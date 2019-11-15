package fr.redby.gallery.service.statistics.repositories;

import fr.redby.gallery.service.statistics.beans.PictureResolutionPerYear;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PictureResolutionPerYearRepository extends MongoRepository<PictureResolutionPerYear, Integer> {
    //
    int STAT_IDENTIFIER = 1;

}
