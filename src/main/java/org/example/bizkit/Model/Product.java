package org.example.bizkit.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer providerId;

    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotEmpty
    @Column(columnDefinition = "varchar(200) not null")
    private String description;

    @NotNull
    @Column(columnDefinition = "decimal(10,2) not null")
    private Double price;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer stockQuantity;
}
