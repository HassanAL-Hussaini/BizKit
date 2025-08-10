package org.example.bizkit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdminInfoDto {
    private String name;
    private String email;
    private String phone;
}
