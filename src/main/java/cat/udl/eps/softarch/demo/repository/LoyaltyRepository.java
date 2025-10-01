package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Loyalty;
import cat.udl.eps.softarch.demo.domain.Customer;
// import cat.udl.eps.softarch.demo.domain.Business; // Uncomment after implementing the business class
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface LoyaltyRepository extends PagingAndSortingRepository<Loyalty, Long>, CrudRepository<Loyalty, Long> {
    // Optional<Loyalty> findByCustomerAndBusiness(@Param("customer") Customer customer, @Param("business") Business business); // Uncomment after implementing the business class
    Optional<Loyalty> findById(Long id);

    // List<Loyalty> findByBusiness(Business business); // Uncomment after implementing the business class
    List<Loyalty> findByCustomer(Customer customer);
    List<Loyalty> findByAccumulatedPointsGreaterThanEqual(Integer points);
    Optional<Loyalty> findFirstByCustomerOrderByStartDateDesc(Customer customer);
}