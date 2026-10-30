package net.nineflas.accountservice.web.rest;



import lombok.RequiredArgsConstructor;
import net.nineflas.accountservice.domain.Account;
import net.nineflas.accountservice.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping
    public List<Account> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        return service.create(account.getCustomerReference(),
                account.getCurrency(), account.getBalance());
    }
}
