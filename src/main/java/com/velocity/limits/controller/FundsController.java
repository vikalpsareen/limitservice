package com.velocity.limits.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velocity.limits.dto.FundLoadRequest;
import com.velocity.limits.dto.FundStatus;
import com.velocity.limits.service.FundLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for handling fund load operations.
 */
@RestController
@RequestMapping("/api/v1/fund-loads")
public class FundsController {

    private static final Logger logger = LoggerFactory.getLogger(FundsController.class);
    @Autowired
    private FundLoadService fundLoadService;

    @Autowired
    private ObjectMapper objectMapper; // Inject ObjectMapper here

    /**
     * Endpoint to process fund load requests from a file.
     *
     * @param file The multipart file containing fund load data in JSON format.
     * @throws IOException If an I/O exception occurs during file processing.
     */
    @PostMapping("/loadFromFile")
    public ResponseEntity<List<FundStatus>> loadFromFile(MultipartFile file)  {

        // 1. Initialize a list to store fund load requests.
        List<FundLoadRequest> fundLoadRequests = new ArrayList<>();

        // 2. Read the file content and convert each line to FundLoadRequest objects.
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {
                FundLoadRequest fundLoadRequest = objectMapper.readValue(line, FundLoadRequest.class);
                fundLoadRequests.add(fundLoadRequest);
            }
        } catch (IOException e) {
            logger.error("Exception occurred while reading file content", e);
            throw new RuntimeException("Exception occurred while processing fund loads");
        }
        // 3. Process each fund load request using the FundLoadService.
        List<FundStatus> fundStatusList = new ArrayList<>();
        for(FundLoadRequest fundLoadRequest : fundLoadRequests)
        {
            fundStatusList.add(fundLoadService.processLoad(fundLoadRequest));
        }
        return ResponseEntity.ok(fundStatusList);
    }
}
