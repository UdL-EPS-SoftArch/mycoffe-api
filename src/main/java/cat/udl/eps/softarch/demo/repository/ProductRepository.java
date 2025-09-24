package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);
    List<Product> findByPriceBetween(double min, double max);
    List<Product> findByRatingGreaterThanEqual(double rating);
    List<Product> findByIsAvailableTrue();
    List<Product> findByIsAvailableFalse();
    List<Product> findByOrder(Order order);
    List<Product> findByBaskets(Basket basket);
    List<Product> findByPromotions( String promotion);
    List<Product> findBySize(String size);
    List<Product> findByKcalLessThanEqual(int kcal);
    List<Product> findByIngredientsContaining(String ingredient);
    List<Product> findByAllergensContaining(String allergen);
}
