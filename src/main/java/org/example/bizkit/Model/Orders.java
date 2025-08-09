package org.example.bizkit.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "status IN ('pending','accepted','rejected','completed','canceled')")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer clientId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer providerId;

    @NotNull
    @Column(columnDefinition = "double not null")
    private Double totalAmount;

    @Pattern(regexp = "^(pending|accepted|canceled|rejected|completed)$", message = "Status must be pending, accepted, rejected, canceled or completed")
    @Column(columnDefinition = "varchar(20)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status = "pending";


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}