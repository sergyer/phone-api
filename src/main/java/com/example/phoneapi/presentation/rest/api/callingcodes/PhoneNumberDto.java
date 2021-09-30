package com.example.phoneapi.presentation.rest.api.callingcodes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PhoneNumberDto {

    @ValidPhoneNumber
    private String number;
}
