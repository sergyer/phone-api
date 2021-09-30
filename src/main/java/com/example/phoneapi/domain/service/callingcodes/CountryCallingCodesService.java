package com.example.phoneapi.domain.service.callingcodes;

import com.example.phoneapi.domain.model.country.Country;

public interface CountryCallingCodesService {

    Country resolve(String code);

}
