package net.nineflas.transactionservice.service;



import lombok.RequiredArgsConstructor;
import net.nineflas.transactionservice.domain.Transaction;
import net.nineflas.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountClient accountClient;

    @Value("${transaction.max-amount}")
    private BigDecimal maxAmount;

    public Transaction create(UUID accountId, BigDecimal amount, String currency,
                              String merchantCategory, String country) {
        checkAmount(amount);
        AccountDto account = accountClient.getAccount(accountId);
        checkAccount(account, currency);
        if (BigDecimal.valueOf(account.balance()).compareTo(amount) < 0) {
            throw new IllegalArgumentException("Solde insuffisant");
        }
        Transaction t = save(accountId, amount, currency, merchantCategory, country);
        accountClient.updateBalance(accountId, -amount.doubleValue());
        return t;
    }

    public List<Transaction> transfer(UUID sourceId, UUID targetId, BigDecimal amount) {
        checkAmount(amount);
        AccountDto source = accountClient.getAccount(sourceId);
        AccountDto target = accountClient.getAccount(targetId);
        checkAccount(source, source.currency());
        checkAccount(target, source.currency());
        if (BigDecimal.valueOf(source.balance()).compareTo(amount) < 0) {
            throw new IllegalArgumentException("Solde insuffisant");
        }
        Transaction debit = save(sourceId, amount, source.currency(), "TRANSFER", null);
        Transaction credit = save(targetId, amount, source.currency(), "TRANSFER", null);
        accountClient.updateBalance(sourceId, -amount.doubleValue());
        accountClient.updateBalance(targetId, amount.doubleValue());
        return List.of(debit, credit);
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public List<Transaction> findByAccount(UUID accountId) {
        return repository.findByAccountId(accountId);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
        if (amount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("Montant supérieur au plafond : " + maxAmount);
        }
    }

    private void checkAccount(AccountDto account, String currency) {
        if (!"ACTIVE".equals(account.status())) {
            throw new IllegalArgumentException("Le compte n'est pas actif");
        }
        if (!account.currency().equals(currency)) {
            throw new IllegalArgumentException("Devise différente de celle du compte");
        }
    }

    private Transaction save(UUID accountId, BigDecimal amount, String currency,
                             String merchantCategory, String country) {
        Transaction t = new Transaction();
        t.setAccountId(accountId);
        t.setAmount(amount);
        t.setCurrency(currency);
        t.setMerchantCategory(merchantCategory);
        t.setCountry(country);
        t.setCreatedAt(Instant.now());
        return repository.save(t);
    }
}
