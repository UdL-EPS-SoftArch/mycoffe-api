package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Basket extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dueño de la relación
    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Customer customer;

    @Override
    public Long getId() {
        return id;
    }
}
