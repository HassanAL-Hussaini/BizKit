package org.example.bizkit.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer orderId;

    @NotNull
    @Column(columnDefinition = "double not null")
    private Double amount;

    @UpdateTimestamp
    @Column(columnDefinition = "date")
    //Done this will be Edited (dated) only if the invoice issued .
    private LocalDate issuedDate;

    @CreationTimestamp
    @Column(columnDefinition = "date not null")
    private LocalDate dueDate;
}
