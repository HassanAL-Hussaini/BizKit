package org.example.bizkit.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "status IN ('active','unactive')")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 30)
    @NotEmpty
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty
    @Size(max = 30)
    @Email
    @Column(columnDefinition = "varchar(30) not null unique")
    private String email;

    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, and a digit"
    )
    @Column(columnDefinition = "varchar(30) not null")
    private String password;

    @NotEmpty
    @Size(min = 10, max = 10)
    @Column(columnDefinition = "varchar(10) not null unique")
    private String phone;

    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null")
    private String companyName;//description

    @NotEmpty
    @Column(columnDefinition = "varchar(100) not null")
    private String address;

    //Done For Ai Recommendation :
    @Column(columnDefinition = "TEXT")
    private String recommendation;


    @NotEmpty
    //the idea here is : same as band or block , if the client dose somthing wrong I will make his state to unactive = Block/band
    @Pattern(regexp = "^(active|unactive)$", message = "Status must be Active Or unactive")
    @Column(columnDefinition = "varchar(20) not null")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status =  "active";
}
