package com.dean.bank_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String firstName;

    private String lastName;

    private String otherName;

    private String gender;

    private String address;

    private String stateOfOrigin;

    private String email;

    private String password;

    private String phoneNumber;

    private String alternatePhoneNumber;

}
