package br.com.tales.domain.dto;

import br.com.tales.domain.enums.AccountStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AccountDTO {
    private LocalDate dtExpiration;
    private LocalDate dtPayment;
    private BigDecimal value;
    private String description;
    private AccountStatus status;
}
