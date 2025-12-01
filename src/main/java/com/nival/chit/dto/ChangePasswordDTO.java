package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for changing user password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
}

