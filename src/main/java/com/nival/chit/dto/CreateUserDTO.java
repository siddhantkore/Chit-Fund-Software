package com.nival.chit.dto;

import com.nival.chit.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    private String name;
    private String username;
    private Long phone;
    private String email;
    private String password;
    private UserRoles role;
}

