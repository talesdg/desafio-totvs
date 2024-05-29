package domain.service;

import br.com.tales.domain.dto.AccountDTO;
import br.com.tales.domain.enums.AccountStatus;
import br.com.tales.domain.model.Account;
import br.com.tales.domain.repository.AccountRepository;
import br.com.tales.domain.service.impl.AccountServiceImpl;
import br.com.tales.infrastracture.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private final static Long DEFAULT_ID = 1L;
    private final static Integer VERIFY_TIME = 1;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateAccount() {
        Account account = new Account();
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Account createdAccount = accountService.createAccount(account);
        assertNotNull(createdAccount);
        verify(accountRepository, times(VERIFY_TIME)).save(account);
    }

    @Test
    public void whenUpdateAccounts() {
        Account existingAccount = new Account();
        existingAccount.setId(DEFAULT_ID);
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(existingAccount));

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setDtExpiration(LocalDate.now());
        accountDTO.setDtPayment(LocalDate.now());
        accountDTO.setValue(BigDecimal.valueOf(100));
        accountDTO.setDescription("Test");
        accountDTO.setStatus(AccountStatus.PAID);

        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        Account updatedAccount = accountService.updateAccounts(DEFAULT_ID, accountDTO);

        assertNotNull(updatedAccount);
        verify(accountRepository, times(VERIFY_TIME)).findById(DEFAULT_ID);
        verify(accountRepository, times(VERIFY_TIME)).save(existingAccount);
    }

    @Test
    public void whenChangeStatusAccount() {
        Account existingAccount = new Account();
        existingAccount.setId(DEFAULT_ID);
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);
        Account updatedAccount = accountService.changeStatusAccount(DEFAULT_ID, AccountStatus.PAID);

        assertNotNull(updatedAccount);
        assertEquals(AccountStatus.PAID, updatedAccount.getStatus());
        verify(accountRepository, times(VERIFY_TIME)).findById(DEFAULT_ID);
        verify(accountRepository, times(VERIFY_TIME)).save(existingAccount);
    }

    @Test
    public void whenGetAccounts() {
        LocalDate dtInit = LocalDate.now();
        LocalDate dtFim = LocalDate.now();
        String description = "Test";
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Account> page = new PageImpl<>(Collections.singletonList(new Account()));
        when(accountRepository.findByDtExpirationBetweenAndDescriptionContaining(dtInit, dtFim, description, pageRequest))
                .thenReturn(page);

        Page<Account> accounts = accountService.getAccounts(dtInit, dtFim, description, 1, 10);

        assertNotNull(accounts);
        assertEquals(1, accounts.getTotalElements());
        verify(accountRepository, times(VERIFY_TIME))
                .findByDtExpirationBetweenAndDescriptionContaining(dtInit, dtFim, description, pageRequest);
    }

    @Test
    public void whenGetAccountById() {
        Account account = new Account();
        account.setId(DEFAULT_ID);
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(DEFAULT_ID);

        assertNotNull(foundAccount);
        assertEquals(DEFAULT_ID, foundAccount.getId());
        verify(accountRepository, times(VERIFY_TIME)).findById(DEFAULT_ID);
    }

    @Test
    public void whenGetTotalPaidPerPeriod() {
        LocalDate dtInit = LocalDate.now();
        LocalDate dtFim = LocalDate.now();
        Account account = new Account();
        account.setValue(BigDecimal.valueOf(100));
        when(accountRepository.findByDtPaymentBetween(dtInit, dtFim))
                .thenReturn(Collections.singletonList(account));

        BigDecimal total = accountService.getTotalPaidPerPeriod(dtInit, dtFim);

        assertNotNull(total);
        assertEquals(BigDecimal.valueOf(100), total);
        verify(accountRepository, times(VERIFY_TIME)).findByDtPaymentBetween(dtInit, dtFim);
    }

    @Test
    public void whenUploadFileAccount() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        accountService.uploadFileAccount(file);

        verify(accountRepository, times(VERIFY_TIME)).saveAll(any());
    }

    @Test
    public void whenAccountNotFoundException() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getAccountById(DEFAULT_ID));
    }

    @Test
    public void whenUploadFileAccount_IOError() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Test IOException"));

        assertThrows(RuntimeException.class, () -> accountService.uploadFileAccount(file));
    }
}
