package net.nineflas.accountservice.service;

import lombok.RequiredArgsConstructor;

import net.nineflas.accountservice.domain.Account;
import net.nineflas.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public Account create(String customerReference, String currency, double balance) {
        Account account = new Account();
        account.setCustomerReference(customerReference);
        account.setCurrency(currency);
        account.setBalance(balance);
        return repository.save(account);
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));
    }

    public Account updateBalance(UUID id, double delta) {
        Account account = findById(id);
        account.setBalance(account.getBalance() + delta);
        return repository.save(account);
    }
}
