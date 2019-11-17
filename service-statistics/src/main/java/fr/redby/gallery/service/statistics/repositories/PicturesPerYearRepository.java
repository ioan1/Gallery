package fr.redby.gallery.service.statistics.repositories;

import fr.redby.gallery.service.statistics.beans.DataType;
import fr.redby.gallery.service.statistics.beans.PicturesPerYear;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PicturesPerYearRepository extends MongoRepository<PicturesPerYear, Integer> {

}
