package com.velocity.limits.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a fund load transaction.
 * This class is annotated with {@code @Entity} to indicate that it is a JPA entity.
 *
 * The {@code FundLoad} class is designed to persistently store information about a fund load,
 * including a unique identifier, customer ID, load amount, and timestamp.
 *
 * The {@code id} field is annotated with {@code @Id} to designate it as the primary key for
 * the entity. This field holds a unique identifier for each fund load transaction.
 *
 * The {@code customerId} field represents the customer ID associated with the fund load transaction.
 *
 * The {@code loadAmount} field stores the amount loaded, represented as a {@code BigDecimal}.
 *
 * The {@code time} field captures the timestamp of the fund load transaction, indicating
 * when the load occurred.
 *
 * The {@code FundLoad} class is annotated with {@code @Data} from Lombok, which generates
 * boilerplate code for standard methods such as getters, setters, equals, hashCode, and toString.
 *
 * @author vsareen
 * @version 1.0
 */
@Entity
@Data
public class FundLoad {
    @Id
    private String id;
    private String customerId;
    private BigDecimal loadAmount;
    private LocalDateTime time;
}
