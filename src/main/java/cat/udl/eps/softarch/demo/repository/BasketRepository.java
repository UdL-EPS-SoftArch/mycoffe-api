package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<User, Long> {
}