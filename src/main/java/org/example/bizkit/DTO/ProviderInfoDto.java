package org.example.bizkit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProviderInfoDto {
    private String name;
    private String email;
    private String phone;
    private String companyName;
    private String address;
    private String CommercialRegistrationNumber;
}
