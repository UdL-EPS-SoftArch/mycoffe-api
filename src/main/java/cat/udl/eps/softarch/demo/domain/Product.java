package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
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




}
