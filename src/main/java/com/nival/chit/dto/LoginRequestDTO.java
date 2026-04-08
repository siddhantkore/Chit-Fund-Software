package com.nival.chit.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Request payload for username/password authentication.
 */
@Getter
@Setter
public class LoginRequestDTO {

    private String username;
    private String password;
}
