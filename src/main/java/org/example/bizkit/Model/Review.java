package org.example.bizkit.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer clientId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer orderId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer productId;

    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer rating;

    @NotNull
    @Column(columnDefinition = "varchar(200) not null unique")
    @Size(min = 10 , max = 200 , message = "comment should be between 10 and 200")
    private String Comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;
}
