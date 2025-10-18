package cat.udl.eps.softarch.demo.repository;

import java.util.Optional;
import cat.udl.eps.softarch.demo.domain.Basket;
import cat.udl.eps.softarch.demo.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BasketRepository extends CrudRepository<Basket, Long>, PagingAndSortingRepository<Basket, Long> {

    Optional<Basket> findByCustomer(Customer customer);
}
