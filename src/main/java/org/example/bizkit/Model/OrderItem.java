package org.example.bizkit.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer orderId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer productId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer quantity;

    @NotNull
    @Column(columnDefinition = "double not null")
    private Double price;
}