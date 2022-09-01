package com.example.clip.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {

    private String user_name;
    private BigDecimal payments_sum;
    private Long new_payments;
    private BigDecimal new_payments_amount;

}
