package net.nineflas.accountservice.web.rest.graphql;


import lombok.RequiredArgsConstructor;
import net.nineflas.accountservice.domain.Account;
import net.nineflas.accountservice.service.AccountService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {

    private final AccountService service;

    @QueryMapping
    public List<Account> accounts() {
        return service.findAll();
    }

    @QueryMapping
    public Account accountById(@Argument String id) {
        return service.findById(UUID.fromString(id));
    }

    @MutationMapping
    public Account createAccount(@Argument String customerReference,
                                 @Argument String currency,
                                 @Argument double balance) {
        return service.create(customerReference, currency, balance);
    }
}
