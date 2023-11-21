package com.velocity.limits.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom JSON deserializer for converting a currency amount with a dollar sign to a {@link BigDecimal}.
 * This deserializer removes the dollar sign before parsing the amount.
 */
public class AmountDeserializer extends JsonDeserializer<BigDecimal> {

    /**
     * Deserialize the JSON value, removing the dollar sign and converting to {@link BigDecimal}.
     *
     * @param jsonParser            JSON parser
     * @param deserializationContext Deserialization context
     * @return The deserialized {@link BigDecimal} value
     * @throws IOException       If an I/O error occurs
     */
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String amountWithDollarSign = jsonParser.getValueAsString();
        String amountWithoutDollarSign = amountWithDollarSign.replace("$", "");
        return new BigDecimal(amountWithoutDollarSign);
    }
}

