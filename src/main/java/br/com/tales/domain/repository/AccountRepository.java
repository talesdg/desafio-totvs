package br.com.tales.domain.repository;

import br.com.tales.domain.enums.AccountStatus;
import br.com.tales.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    Account save(Account account);
    Optional<Account> findById(Long id);
    Page<Account> findByDtExpirationBetweenAndDescriptionContaining(LocalDate startDate, LocalDate endDate, String description, Pageable pageRequest);
//    List<Account> findByStatus(AccountStatus status);
    List<Account> findByDtPaymentBetween(LocalDate startDate, LocalDate endDate);
}
