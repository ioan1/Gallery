package fr.redby.gallery.service.statistics.repositories;

import fr.redby.gallery.service.statistics.beans.DataType;
import fr.redby.gallery.service.statistics.beans.PicturesPerYear;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PicturesPerYearRepository extends MongoRepository<PicturesPerYear, Integer> {

    /**
     * The following view is needed:
     *
     db.exifData.aggregate([
     {
     "$match": {
     "dateTaken": {
     "$exists": true,
     "$ne": null
     }
     }
     },
     {
     "$group": {
     "_id": {
     "year": {
     "$year": "$dateTaken"
     }
     },
     "y": {
     "$sum": 1
     },
     "date": {
     "$first": "$dateTaken"
     }
     }
     },
     {
     "$project": {
     "_id": {
     "$dateToString": {
     "format": "%Y",
     "date": "$date"
     }
     },
     "x": {
     "$dateToString": {
     "format": "%Y",
     "date": "$date"
     }
     },
     "y": 1
     }
     },
     {
     $sort:{"_id":1}
     }
     ]).saveAsView("picturesPerYear");
     */
}
