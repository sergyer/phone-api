package com.example.phoneapi.infrastructure;

import com.example.phoneapi.domain.model.country.Country;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlParserTest {

    private final HtmlParser htmlParser = new HtmlParser();

    @Test
    void givenContentAndCodesMap_whenCalled_thenPopulated() throws IOException {
        //Test data
        final var testHtml = new String(getClass().getResourceAsStream("/test-codes.html").readAllBytes(), StandardCharsets.UTF_8);
        final var callingCodesMap = new ConcurrentHashMap<String, Country>();
        final var armenia = new Country("Armenia");
        final var georgia = new Country("Georgia");
        //Run test scenario
        htmlParser.parse(testHtml, callingCodesMap);
        //Asserts
        assertThat(callingCodesMap).isNotEmpty();
        assertThat(callingCodesMap.get("+374")).isEqualTo(armenia);
        assertThat(callingCodesMap.get("+995")).isEqualTo(georgia);
    }
}
