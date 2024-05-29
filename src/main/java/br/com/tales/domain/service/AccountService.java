package br.com.tales.domain.service;

import br.com.tales.domain.dto.AccountDTO;
import br.com.tales.domain.enums.AccountStatus;
import br.com.tales.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AccountService {
    Account createAccount(Account ac);
    Account updateAccounts(Long id, AccountDTO accountUp);
    Account changeStatusAccount(Long id, AccountStatus status);
    Page<Account> getAccounts(LocalDate dtInit, LocalDate dtFim, String description, Integer page, Integer linesPerPage);
    Account getAccountById(Long id);
    BigDecimal getTotalPaidPerPeriod(LocalDate dtInit, LocalDate dtFim);
    void uploadFileAccount(MultipartFile file);
}
