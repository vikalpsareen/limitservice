package com.velocity.limits.repository;

import com.velocity.limits.model.FundLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing {@link FundLoad} entities.
 * This interface extends {@link JpaRepository}, providing CRUD functionality for the
 * {@link FundLoad} entity with primary key of type {@link Long}.
 *
 * The repository is annotated with {@code @Repository} to indicate that it is a Spring
 * Data repository component.
 *
 * The interface includes a custom query method, {@code findByCustomerIdAndTimeBetween},
 * to retrieve a list of fund loads for a specific customer ID within a given time range.
 * This method is automatically implemented by Spring Data JPA based on its naming convention.
 *
 * @author vsareen
 * @version 1.0
 */
@Repository
public interface FundLoadRepository extends JpaRepository<FundLoad, Long> {

    /**
     * Retrieves a list of fund loads for a specific customer ID within the specified time range.
     *
     * @param customerId The customer ID for which to retrieve fund loads.
     * @param start      The start of the time range.
     * @param end        The end of the time range.
     * @return A list of fund loads that match the specified criteria.
     */
    List<FundLoad> findByCustomerIdAndTimeBetween(String customerId, LocalDateTime start, LocalDateTime end);

}
