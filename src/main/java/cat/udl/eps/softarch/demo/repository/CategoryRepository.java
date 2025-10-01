package cat.udl.eps.softarch.demo.repository;


import cat.udl.eps.softarch.demo.domain.Category;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepository {

    List<Category> findByName(String name);
    List<Category> findAll();

}
