package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

//Attributes and methods of Product Class that extends UriEntity
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends UriEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private String description;
    private int stock;
    private BigDecimal price;
    private String brand;
    private String size;
    private String barcode;
    private BigDecimal tax;
    private boolean isAvailable;
    private String promotions;
    private String discount;
    private int kcal;
    private int carbs;
    private int proteins;
    private int fats;
    @ElementCollection
    private List<String> ingredients;
    @ElementCollection
    private List<String> allergens;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double rating;

    //TODO private Category category;

    @ManyToMany
    private List<Basket> baskets;

    @ManyToMany
    private List<Order> orders;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Loyalty> loyalties;

    @ManyToOne
    private Inventory inventory;




}
