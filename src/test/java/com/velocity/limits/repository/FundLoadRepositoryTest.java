package com.velocity.limits.repository;

import com.velocity.limits.model.FundLoad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test") // Optional: Use a separate profile for testing
class FundLoadRepositoryTest {

    @Autowired
    private FundLoadRepository fundLoadRepository;

    @Test
    void findByCustomerIdAndTimeBetween() {
        // Given
        String customerId = "123";
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now();

        // Save test data to the database
        FundLoad fundLoad1 = new FundLoad();
        fundLoad1.setId("1");
        fundLoad1.setCustomerId(customerId);
        fundLoad1.setLoadAmount(new BigDecimal("1000"));
        fundLoad1.setTime(start.plusMinutes(15));
        fundLoadRepository.save(fundLoad1);

        FundLoad fundLoad2 = new FundLoad();
        fundLoad2.setId("2");
        fundLoad2.setCustomerId(customerId);
        fundLoad2.setLoadAmount(new BigDecimal("1500"));
        fundLoad2.setTime(end.minusMinutes(30));
        fundLoadRepository.save(fundLoad2);

        // When
        List<FundLoad> result = fundLoadRepository.findByCustomerIdAndTimeBetween(customerId, start, end);

        // Then
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("1000"), result.get(0).getLoadAmount());
        assertEquals(new BigDecimal("1500"), result.get(1).getLoadAmount());
    }
}

