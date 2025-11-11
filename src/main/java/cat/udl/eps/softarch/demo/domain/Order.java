package cat.udl.eps.softarch.demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="order")
@Data
public class Order extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
