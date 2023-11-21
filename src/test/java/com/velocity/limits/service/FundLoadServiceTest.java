package com.velocity.limits.service;

import com.velocity.limits.dto.FundLoadRequest;
import com.velocity.limits.model.FundLoad;
import com.velocity.limits.repository.FundLoadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FundLoadServiceTest {

    @Mock
    private FundLoadRepository fundLoadRepository;

    @Value("${max.weekly.load.amount}")
    private String maxWeeklyLoadAmount;

    @Value("${max.daily.load.amount}")
    private String maxDailyLoadAmount;

    @InjectMocks
    private FundLoadService fundLoadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fundLoadService.setMaxDailyLoadAmount("5000");
        fundLoadService.setMaxWeeklyLoadAmount("20000");
    }

    @Test
    void testIsLoadAccepted() {
        // Mocking data
        FundLoad fundLoad = new FundLoad();
        fundLoad.setId("1");
        fundLoad.setCustomerId("1");
        fundLoad.setLoadAmount(new BigDecimal("1000"));
        fundLoad.setTime(LocalDateTime.now());

        List<FundLoad> dailyLoads = new ArrayList<>();
        dailyLoads.add(fundLoad);

        List<FundLoad> weeklyLoads = new ArrayList<>();
        weeklyLoads.add(fundLoad);

        // Mocking repository methods
        when(fundLoadRepository.findByCustomerIdAndTimeBetween(anyString(), any(), any())).thenReturn(dailyLoads);
        when(fundLoadRepository.findByCustomerIdAndTimeBetween(anyString(), any(), any())).thenReturn(weeklyLoads);


        // Testing isLoadAccepted method
        assertTrue(fundLoadService.isLoadAccepted(fundLoad));

        // Verifying repository methods were called
        verify(fundLoadRepository, times(2)).findByCustomerIdAndTimeBetween(anyString(), any(), any());
    }

    @Test
    void testProcessLoad() {
        // Mocking data
        FundLoadRequest fundLoadRequest = new FundLoadRequest();
        fundLoadRequest.setId("1");
        fundLoadRequest.setCustomer_id("1");
        fundLoadRequest.setLoad_amount(new BigDecimal("1000"));
        fundLoadRequest.setTime(LocalDateTime.now());

        // Testing processLoad method
        fundLoadService.processLoad(fundLoadRequest);

        // Verifying repository method was called
        verify(fundLoadRepository, times(1)).save(any());
    }

    @Test
    void testProcessLoadWhenLimitExceed() {
        // Mocking data
        FundLoadRequest fundLoadRequest = new FundLoadRequest();
        fundLoadRequest.setId("1");
        fundLoadRequest.setCustomer_id("1");
        fundLoadRequest.setLoad_amount(new BigDecimal("6000"));  // Exceeds daily limit
        fundLoadRequest.setTime(LocalDateTime.now());

        // Testing processLoad method
        fundLoadService.processLoad(fundLoadRequest);

        // Verifying repository method was not called
        verify(fundLoadRepository, never()).save(any());
    }
}

