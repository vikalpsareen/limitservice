package com.velocity.limits.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.velocity.limits.util.AmountDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a request to load funds into a customer's account.
 * This class is used to deserialize JSON payloads into Java objects.
 *
 * The {@code FundLoadRequest} class is designed to capture the essential information
 * required for processing fund load requests, including the unique identifier, customer ID,
 * load amount, and the timestamp of the request.
 *
 * The {@code id} field represents a unique identifier for the fund load request.
 *
 * The {@code customer_id} field stores the customer ID associated with the fund load request.
 *
 * The {@code load_amount} field is annotated with {@code @JsonDeserialize} to specify a custom
 * deserializer, {@code AmountDeserializer}, ensuring proper conversion of the load amount from
 * its JSON representation to a {@code BigDecimal}.
 *
 * The {@code time} field is annotated with {@code @JsonFormat} to define the pattern for
 * date-time formatting during JSON serialization and deserialization. It uses the
 * "yyyy-MM-dd'T'HH:mm:ss'Z'" pattern to represent timestamps in UTC.
 *
 * The {@code FundLoadRequest} class is annotated with {@code @Data} from Lombok, which generates
 * boilerplate code for standard methods such as getters, setters, equals, hashCode, and toString.
 *
 * @author vsareen
 * @version 1.0
 */
@Data
public class FundLoadRequest {
    private String id;
    private String customer_id;
    @JsonDeserialize(using = AmountDeserializer.class)
    private BigDecimal load_amount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime time;
}
