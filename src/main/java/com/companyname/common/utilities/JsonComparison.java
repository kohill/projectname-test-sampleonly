package com.companyname.common.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.companyname.verification.CustomAssertions.assertThat;

public class JsonComparison {

    public static void compareJsonIgnoringTags(String expectedJson, String actualJson, String... keysToExclude) {
        // Parse JSON strings to JSON objects
        JsonObject expectedJsonObj = JsonParser.parseString(expectedJson).getAsJsonObject();
        JsonObject actualJsonObj = JsonParser.parseString(actualJson).getAsJsonObject();

        assertThat(actualJsonObj).usingRecursiveComparison()
                .ignoringFields(keysToExclude).ignoringActualNullFields().ignoringExpectedNullFields()
                .isEqualTo(expectedJsonObj);
    }


}