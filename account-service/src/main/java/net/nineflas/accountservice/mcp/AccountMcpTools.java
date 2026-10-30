package net.nineflas.accountservice.mcp;


import lombok.RequiredArgsConstructor;
import net.nineflas.accountservice.domain.Account;
import net.nineflas.accountservice.service.AccountService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountMcpTools {

    private final AccountService service;

    @Tool(description = "Liste tous les comptes bancaires")
    public List<Account> listAccounts() {
        return service.findAll();
    }

    @Tool(description = "Retourne un compte bancaire à partir de son identifiant")
    public Account getAccount(@ToolParam(description = "UUID du compte") String id) {
        return service.findById(UUID.fromString(id));
    }

    @Tool(description = "Crée un nouveau compte bancaire")
    public Account createAccount(
            @ToolParam(description = "Référence du client") String customerReference,
            @ToolParam(description = "Devise, par exemple MAD") String currency,
            @ToolParam(description = "Solde initial") double balance) {
        return service.create(customerReference, currency, balance);
    }
}
