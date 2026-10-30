package net.nineflas.transactionservice.web.rest.graphql;



import lombok.RequiredArgsConstructor;
import net.nineflas.transactionservice.domain.Transaction;
import net.nineflas.transactionservice.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {

    private final TransactionService service;

    @QueryMapping
    public List<Transaction> transactions(@Argument String accountId) {
        return accountId == null ? service.findAll()
                : service.findByAccount(UUID.fromString(accountId));
    }

    @MutationMapping
    public Transaction createTransaction(@Argument String accountId, @Argument double amount,
                                         @Argument String currency,
                                         @Argument String merchantCategory,
                                         @Argument String country) {
        return service.create(UUID.fromString(accountId), BigDecimal.valueOf(amount),
                currency, merchantCategory, country);
    }

    @MutationMapping
    public List<Transaction> transfer(@Argument String sourceAccountId,
                                      @Argument String targetAccountId,
                                      @Argument double amount) {
        return service.transfer(UUID.fromString(sourceAccountId),
                UUID.fromString(targetAccountId), BigDecimal.valueOf(amount));
    }
}
