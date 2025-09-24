package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String> {

    List<Product> findByName(String name);
    

}
