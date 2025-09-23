package cat.udl.eps.softarch.demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name="order")
@Data
public class Order extends UriEntity<String>{

    /**
     * All attributes are String because we don't know exactly what kind of variable they are.
     */
    @Setter
    @Id
    private String id;

    @NotBlank
    private String created;

    @NotBlank
    private String serveWhen;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private enum Status{
        RECEIVED, CANCELLED, IN_PROCESS, READY, PICKED
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Product and Customer will be implemented with and instance of his class.
     * For now we will leave it as String for code clearness.
     */
    @ManyToMany
    private String product;

    @ManyToOne
    private String customer;

}
