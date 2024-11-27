package sa.m.ntd.calculator.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class CalculatorUser {

    public CalculatorUser(String username, String password, String status) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Column(name = "id", nullable = false)
    private String id;

    @Id
    @Size(min = 6, max = 200)
    @Email
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Size(min = 8, max = 2048)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private String status;
}
