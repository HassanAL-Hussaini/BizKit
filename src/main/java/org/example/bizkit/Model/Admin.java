package org.example.bizkit.Model;

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
@Check(constraints = "role = 'admin' or role = 'super admin'")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 30)
    @NotEmpty
    @Column(columnDefinition = "varchar(30) not null")
    private String name;


    @NotEmpty
    @Size(max = 30)
    @Email(message = "wrong email")
    @Column(columnDefinition = "varchar(30) not null unique")
    private String email;

    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, and a digit"
    )
    @Column(columnDefinition = "varchar(30) not null")
    private String password;

    @Pattern(regexp = "^(admin|super admin)$" , message = "Role must be Admin or Super Admin")
    @Column(columnDefinition = "varchar(30) not null")
    private String role;

    @NotEmpty
    @Size(min = 10, max = 10)
    @Column(columnDefinition = "varchar(10) not null unique")
    private String phone;
}
