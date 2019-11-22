package fr.redby.gallery.service.albums.repository;

import fr.redby.gallery.service.albums.bean.Album;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AlbumRepository extends MongoRepository<Album, String> {

    @Query("{category: ?0 })")
    List<Album> findByCategory(final String category);

}
