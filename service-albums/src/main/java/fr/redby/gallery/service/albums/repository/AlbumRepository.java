package fr.redby.gallery.service.albums.repository;

import fr.redby.gallery.service.albums.bean.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album, String> {
    //
}
