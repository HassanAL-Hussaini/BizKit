package org.example.bizkit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ReviewInfoDto {

    private String ClientName;
    private String productName;
    private Integer rating;
    private String Comment;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
