package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> findByRatingGreaterThanEqual(Double rating);
    List<Product> findByAvailable(boolean available);
    List<Product> findByPromotions( String promotion);
    List<Product> findBySize(String size);
    List<Product> findByKcalLessThanEqual(int kcal);
    List<Product> findByIngredientsContaining(String ingredient);
    List<Product> findByAllergensContaining(String allergen);
    //TODO  List<Product> findByOrders(Order order);
    //      List<Product> findByBaskets(Basket basket);

}
