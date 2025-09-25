package cat.udl.eps.softarch.demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="order")
@Data
public class Order extends UriEntity<String>{

    /**
     * Attributes are now defined but they are not probably the final variable.
     */

    @Setter
    @Id
    private String id;

    @NotBlank
    private LocalDateTime created;

    @NotBlank
    private LocalDateTime serveWhen;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        RECEIVED, CANCELLED, IN_PROCESS, READY, PICKED
    }

    /**
     * Product and Customer are defined but we don't have the classes explicitly.
     */

    @ManyToMany
    private Set<Product> products;

    @ManyToOne
    private Customer customer;

    @Override
    public String getId() {
        return id;
    }

}
