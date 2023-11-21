package com.velocity.limits.service;

import com.velocity.limits.dto.FundLoadRequest;
import com.velocity.limits.dto.FundStatus;
import com.velocity.limits.model.FundLoad;
import com.velocity.limits.repository.FundLoadRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Service class responsible for processing fund loads and enforcing load limits.
 * This class contains methods to convert and process fund load requests, checking
 * and enforcing daily and weekly load limits before saving them to the repository.
 * <p>
 * The class is annotated with {@code @Service} to indicate that it is a Spring service
 * component and is autowiring the {@link FundLoadRepository} for data access.
 * <p>
 * Author: vsareen
 * Version: 1.0
 */
@Data
@Service
public class FundLoadService {

    private static final Logger logger = LoggerFactory.getLogger(FundLoadService.class);

    @Autowired
    private FundLoadRepository fundLoadRepository;

    @Value("${max.weekly.load.amount}")
    private String maxWeeklyLoadAmount;

    @Value("${max.daily.load.amount}")
    private String maxDailyLoadAmount;

    /**
     * Processes a fund load request, converting it to an entity and checking
     * if the load is accepted based on daily and weekly load limits.
     *
     * @param fundLoadRequest The fund load request to be processed.
     */
    public FundStatus processLoad(FundLoadRequest fundLoadRequest) {
        try {
            FundLoad fundLoad = mapToEntity(fundLoadRequest);

            if (isLoadAccepted(fundLoad)) {
                fundLoadRepository.save(fundLoad);
                return fundAccepted(fundLoad);
            } else {
                return fundRejected(fundLoad);
            }
        } catch (Exception e) {
            logger.error("Error processing fund load", e);
            throw new RuntimeException("Error processing fund load");
        }
    }

    /**
     * Checks if a fund load is accepted based on daily and weekly load limits.
     *
     * @param fundLoad The fund load to be checked.
     * @return {@code true} if the load is accepted, {@code false} otherwise.
     */
    public boolean isLoadAccepted(FundLoad fundLoad) {
        // Logic to check and enforce load limits

        // Check daily load limit
        LocalDate loadDate = fundLoad.getTime().toLocalDate();

        // Check daily load limit
        LocalDateTime startOfDay = LocalDateTime.of(loadDate, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(loadDate, LocalTime.MAX);

        List<FundLoad> dailyLoads = fundLoadRepository.findByCustomerIdAndTimeBetween(
                fundLoad.getCustomerId(), startOfDay, endOfDay);

        if (dailyLoads.size() >= 3) { // This can be taken from configuration file
            return false; // Exceeded the maximum number of loads per day
        }

        BigDecimal dailyTotal = dailyLoads.stream()
                .map(FundLoad::getLoadAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if ((dailyTotal.add(fundLoad.getLoadAmount())).compareTo(new BigDecimal(maxDailyLoadAmount)) > 0) {
            return false; // Exceeded the maximum daily load amount
        }
        // Check weekly load limit
        LocalDateTime startOfWeek = LocalDateTime.of(loadDate, LocalTime.MIN)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = LocalDateTime.of(loadDate, LocalTime.MAX)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<FundLoad> weeklyLoads = fundLoadRepository.findByCustomerIdAndTimeBetween(
                fundLoad.getCustomerId(), startOfWeek, endOfWeek);

        if (weeklyLoads.size() >= 3) {
            return false; // Exceeded the maximum number of loads per week
        }

        BigDecimal weeklyTotal = weeklyLoads.stream()
                .map(FundLoad::getLoadAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(fundLoad.getLoadAmount());

        if (weeklyTotal.add(fundLoad.getLoadAmount()).compareTo(new BigDecimal(maxWeeklyLoadAmount)) > 0) { // This can be taken from configuration file
            return false; // Exceeded the maximum weekly load amount
        }


        return true; // Load is accepted based on constraints
    }


    /**
     * Helper method to map FundLoadRequest to FundLoadEntity.
     *
     * @param fundLoadRequest The fund load request to be mapped.
     * @return The mapped FundLoad entity.
     */
    private FundLoad mapToEntity(FundLoadRequest fundLoadRequest) {
        FundLoad entity = new FundLoad();
        entity.setId(fundLoadRequest.getId());
        entity.setCustomerId(fundLoadRequest.getCustomer_id());
        entity.setLoadAmount(fundLoadRequest.getLoad_amount());
        entity.setTime(fundLoadRequest.getTime());
        return entity;
    }

    /**
     * Checks if a fund load is accepted and returns the corresponding FundStatus.
     *
     * @param fundLoad The fund load to be checked.
     * @return The FundStatus indicating whether the fund load is accepted.
     */
    private FundStatus fundAccepted(FundLoad fundLoad) {
        return printOutput(fundLoad, true);
    }

    /**
     * Checks if a fund load is rejected and returns the corresponding FundStatus.
     *
     * @param fundLoad The fund load to be checked.
     * @return The FundStatus indicating whether the fund load is rejected.
     */
    private FundStatus fundRejected(FundLoad fundLoad) {
        return printOutput(fundLoad, false);
    }

    /**
     * Creates a FundStatus object based on the given fund load and acceptance status.
     *
     * @param fundLoad The fund load for which to create the FundStatus.
     * @param accepted The acceptance status of the fund load.
     * @return The FundStatus object representing the outcome of the fund load processing.
     */
    private FundStatus printOutput(FundLoad fundLoad, boolean accepted) {
        return new FundStatus(fundLoad.getId(), fundLoad.getCustomerId(), accepted);
    }
}
