package net.nineflas.transactionservice.mcp;



import lombok.RequiredArgsConstructor;
import net.nineflas.transactionservice.domain.Transaction;
import net.nineflas.transactionservice.service.TransactionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionMcpTools {

    private final TransactionService service;

    @Tool(description = "Liste toutes les transactions")
    public List<Transaction> listTransactions() {
        return service.findAll();
    }

    @Tool(description = "Liste les transactions d'un compte")
    public List<Transaction> getTransactionsByAccount(
            @ToolParam(description = "UUID du compte") String accountId) {
        return service.findByAccount(UUID.fromString(accountId));
    }

    @Tool(description = "Enregistre une transaction sur un compte")
    public Transaction createTransaction(
            @ToolParam(description = "UUID du compte") String accountId,
            @ToolParam(description = "Montant strictement positif") double amount,
            @ToolParam(description = "Devise, par exemple MAD") String currency,
            @ToolParam(description = "Catégorie du marchand") String merchantCategory,
            @ToolParam(description = "Code pays, par exemple MA") String country) {
        return service.create(UUID.fromString(accountId), BigDecimal.valueOf(amount),
                currency, merchantCategory, country);
    }

    @Tool(description = "Effectue un virement d'un compte vers un autre")
    public List<Transaction> transfer(
            @ToolParam(description = "UUID du compte source") String sourceAccountId,
            @ToolParam(description = "UUID du compte cible") String targetAccountId,
            @ToolParam(description = "Montant du virement") double amount) {
        return service.transfer(UUID.fromString(sourceAccountId),
                UUID.fromString(targetAccountId), BigDecimal.valueOf(amount));
    }
}
