package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Stack;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "Category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category extends UriEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty
    private String name;

    @Column(length = 200)
    private String description;

}
