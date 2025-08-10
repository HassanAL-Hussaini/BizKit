package org.example.bizkit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInfoDto {
    private String name;
    private String description;
    private Double price;
    private String providerName;
    private Integer providerId;
}
