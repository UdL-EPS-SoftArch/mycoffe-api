package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;

//Attributes and methods of Product Class that extends UriEntity
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends UriEntity<Long>{

    @Id
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
    private boolean available;
    private String promotions;
    private String discount;
    private int Kcal;
    private int Carbs;
    private int Proteins;
    private int Fats;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergens;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double rating;

    //TODO private Category category;

    @ManyToMany
    private Basket basket;

    @ManyToMany
    private Order order;

    @OneToMany(cascade = CascadeType.ALL)
    private Loyalty loyalty;

    @ManyToOne
    private Inventory inventory;




}
