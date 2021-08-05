package com.radoslawzerek.bigbetfrontend.dto;

import com.radoslawzerek.bigbetfrontend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private LocalDate created;
    private BigDecimal balance;

    public UserDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = Role.USER;
        created = LocalDate.now();
        balance = new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP);
    }
}
