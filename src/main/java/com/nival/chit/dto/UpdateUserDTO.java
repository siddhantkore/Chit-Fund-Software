package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating user profile information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String name;
    private Long phone;
    private String email;
}

