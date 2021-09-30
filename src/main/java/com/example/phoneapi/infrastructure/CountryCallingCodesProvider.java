package com.example.phoneapi.infrastructure;

import com.example.phoneapi.domain.model.country.Country;
import com.example.phoneapi.domain.service.callingcodes.CountryCallingCodesService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.Assert.*;

@Component
class CountryCallingCodesProvider implements CountryCallingCodesService {

    private final WebClient webClient;

    private final WikipediaProperties wikipediaProperties;

    private final HtmlParser parser;

    private final Map<String, Country> callingCodesMap;

    public CountryCallingCodesProvider(final WebClient webClient, final WikipediaProperties wikipediaProperties, final HtmlParser parser, final Map<String, Country> callingCodesMap) {
        this.webClient = webClient;
        this.wikipediaProperties = wikipediaProperties;
        this.parser = parser;
        this.callingCodesMap = callingCodesMap;
    }

    @PostConstruct
    void populate() {
        final WebClient.RequestHeadersSpec<?> uri = webClient.get().uri(wikipediaProperties.getUrl());
        final var wikiResponseDtoMono = uri.exchangeToMono(clientResponse -> clientResponse.bodyToMono(WikiResponseDto.class));
        final var responseDto = wikiResponseDtoMono.block();
        final var alphabeticalListingIndex = responseDto.getParse().getSections().stream().filter(pageSection -> pageSection.getLine().contains(wikipediaProperties.getPrefix())).findAny().orElseThrow(() -> new RuntimeException("Couldn't retrieve calling codes data")).getIndex();
        final var wikiResponseDto = webClient.get().uri(String.format(wikipediaProperties.getTableUrl(), alphabeticalListingIndex)).exchangeToMono(clientResponse -> clientResponse.bodyToMono(WikiResponseDto.class)).block();
        parser.parse(wikiResponseDto.getParse().getText().getText(), callingCodesMap);
    }


    @Override
    public Country resolve(final String code) {
        hasText(code, "Code should not be null/empty");
        if (code.charAt(1) == '1') {
            return Optional.ofNullable(callingCodesMap.get(code.substring(0, 5))).orElseGet(() -> callingCodesMap.get(code.substring(0, 2)));
        }
        if (code.charAt(1) == '7') {
            return Optional.ofNullable(callingCodesMap.get(code.substring(0, 3))).orElseGet(() -> callingCodesMap.get(code.substring(0, 2)));
        }
        final var substring61 = code.substring(0, 3);
        if (substring61.equals("+61")) {
            return Optional.ofNullable(callingCodesMap.get(code.substring(0, 8))).orElseGet(() -> callingCodesMap.get(substring61));
        }
        final var substring39 = code.substring(0, 3);
        if (substring39.equals("+39")) {
            return Optional.ofNullable(callingCodesMap.get(code.substring(0, 8))).orElseGet(() -> callingCodesMap.get(substring39));
        }
        return Optional.ofNullable(matchForSevenDigits(code).or(() -> matchForSixDigits(code)).or(() -> matchForFourDigits(code)).orElseGet(() -> matchForThreeDigits(code))).orElse(new Country("Unknown Country"));
    }

    private Country matchForThreeDigits(final String code) {
        return callingCodesMap.get(code.substring(0, 3));
    }

    private Optional<Country> matchForFourDigits(final String code) {
        return Optional.ofNullable(callingCodesMap.get(code.substring(0, 4)));
    }

    private Optional<Country> matchForSixDigits(final String code) {
        return Optional.ofNullable(callingCodesMap.get(code.substring(0, 6)));
    }

    private Optional<Country> matchForSevenDigits(final String code) {
        return Optional.ofNullable(callingCodesMap.get(code.substring(0, 7)));
    }
}

