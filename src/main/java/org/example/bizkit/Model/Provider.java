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

public class Provider {
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
    private String companyName;

    //TODO السجل الضريبي مهم
    // i want to show it up in the invoice + make the invoice show up as PDF.
    @NotEmpty
    @Column(columnDefinition = "varchar(100) not null")
    private String address;

    //Admin just change the isActive variable to add the provider to the website.
    @Column(columnDefinition = "boolean not null")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //user can't send this with json and make it true .
    private Boolean isActive = false;
    //any provider that is unactive the website will never show his products until the admin give him the access
}