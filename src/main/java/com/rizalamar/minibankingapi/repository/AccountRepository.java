package com.rizalamar.minibankingapi.repository;

import com.rizalamar.minibankingapi.domain.Account;
import com.rizalamar.minibankingapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findAllByUser(User user);

    User user(User user);
}
