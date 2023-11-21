package com.velocity.limits.dto;

import lombok.Data;

@Data
public class FundStatus {

    private String id;
    private String customer_id;
    private boolean accepted;

    public FundStatus(String id, String customer_id, boolean accepted) {
        this.id = id;
        this.customer_id = customer_id;
        this.accepted = accepted;
    }
}

