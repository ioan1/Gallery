
package fr.redby.gallery.servicepictures.repositories;

import fr.redby.gallery.servicepictures.bean.ExifData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExifDataRepository extends MongoRepository<ExifData, String> {
    //
}
