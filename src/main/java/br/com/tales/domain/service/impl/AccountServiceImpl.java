package br.com.tales.domain.service.impl;

import br.com.tales.domain.dto.AccountDTO;
import br.com.tales.domain.enums.AccountStatus;
import br.com.tales.domain.model.Account;
import br.com.tales.domain.repository.AccountRepository;
import br.com.tales.domain.service.AccountService;
import br.com.tales.infrastracture.csv.AccountCSV;
import br.com.tales.infrastracture.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account createAccount(Account ac) {
        return accountRepository.save(ac);
    }

    @Override
    public Account updateAccounts(Long id, AccountDTO accountUpdated) {
        Account ac = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found with ID: " + id));

        if (accountUpdated.getDtExpiration() != null) {
            ac.setDtExpiration(accountUpdated.getDtExpiration());
        }
        if (accountUpdated.getDtPayment() != null) {
            ac.setDtPayment(accountUpdated.getDtPayment());
        }
        if (accountUpdated.getValue() != null) {
            ac.setValue(accountUpdated.getValue());
        }
        if (accountUpdated.getDescription() != null) {
            ac.setDescription(accountUpdated.getDescription());
        }
        if (accountUpdated.getStatus() != null) {
            ac.setStatus(accountUpdated.getStatus());
        }
        return accountRepository.save(ac);
    }

    @Override
    public Account changeStatusAccount(Long id, AccountStatus status) {
        Account ac = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found with ID: " + id));
        ac.setStatus(status);
        return accountRepository.save(ac);
    }

    @Override
    public Page<Account> getAccounts(LocalDate dtInit, LocalDate dtFim, String description, Integer page, Integer linesPerPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, linesPerPage);
        return accountRepository.findByDtExpirationBetweenAndDescriptionContaining(dtInit, dtFim, description, pageRequest);

    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found with ID: " + id));
    }

    @Override
    public BigDecimal getTotalPaidPerPeriod(LocalDate dataInicio, LocalDate dataFim) {
        List<Account> paidBills = accountRepository.findByDtPaymentBetween(dataInicio, dataFim);
        return paidBills.stream().map(Account::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void uploadFileAccount(MultipartFile file) {
        try {
            List<Account> accounts = AccountCSV.csvToAccount(file.getInputStream());
            accountRepository.saveAll(accounts);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo csv: " + e.getMessage());
        }
    }

}
