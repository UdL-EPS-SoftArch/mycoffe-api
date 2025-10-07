package cat.udl.eps.softarch.demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private LocalDateTime created;

    @NotNull
    private LocalDateTime serveWhen;

    @NotBlank
    private String paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        RECEIVED, CANCELLED, IN_PROCESS, READY, PICKED
    }

    @ManyToMany
    private Set<Product> products;

    @ManyToOne
    private Customer customer;

    @Override
    public String getId() {
        return id;
    }

}
