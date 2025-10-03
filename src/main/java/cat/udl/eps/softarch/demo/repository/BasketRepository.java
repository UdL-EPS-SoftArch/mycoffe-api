package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Basket;
import cat.udl.eps.softarch.demo.domain.Customer;
import cat.udl.eps.softarch.demo.domain.Product;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource
public interface BasketRepository extends CrudRepository<Basket, Long>, PagingAndSortingRepository<Basket, Long> {

    Optional<Basket> findByCustomer(Customer customer);
    Optional<Basket> findByCustomerId(Long customerId);

    boolean existsByCustomer(Customer customer);

    void deleteByCustomer(Customer customer);
}