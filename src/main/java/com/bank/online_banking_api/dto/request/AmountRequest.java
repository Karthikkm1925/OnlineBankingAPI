package com.bank.online_banking_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AmountRequest {

    @NotNull
    private BigDecimal amount;
 
}
