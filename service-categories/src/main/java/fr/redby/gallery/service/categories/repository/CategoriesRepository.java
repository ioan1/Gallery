package fr.redby.gallery.service.categories.repository;

import fr.redby.gallery.service.categories.bean.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoriesRepository extends MongoRepository<Category, String> {
    //
}
