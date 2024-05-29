package br.com.tales.domain.model;

import br.com.tales.domain.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dtExpiration;
    private LocalDate dtPayment;
    private BigDecimal value;
    private String description;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Account(CSVRecord record){
        this.dtExpiration = LocalDate.parse(record.get("date_expiration"));
        this.value = BigDecimal.valueOf(Long.parseLong(record.get("value")));
        this.description = record.get("description");
        this.status = AccountStatus.OPEN;
    }

    public Account() {

    }
}
