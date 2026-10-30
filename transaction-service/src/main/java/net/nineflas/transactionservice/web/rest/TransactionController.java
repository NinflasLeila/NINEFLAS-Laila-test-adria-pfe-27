package net.nineflas.transactionservice.web.rest;



import lombok.RequiredArgsConstructor;
import net.nineflas.transactionservice.domain.Transaction;
import net.nineflas.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    public record TransferRequest(UUID sourceAccountId, UUID targetAccountId, BigDecimal amount) {}

    @PostMapping
    public Transaction create(@RequestBody Transaction t) {
        return service.create(t.getAccountId(), t.getAmount(), t.getCurrency(),
                t.getMerchantCategory(), t.getCountry());
    }

    @PostMapping("/transfer")
    public List<Transaction> transfer(@RequestBody TransferRequest req) {
        return service.transfer(req.sourceAccountId(), req.targetAccountId(), req.amount());
    }

    @GetMapping
    public List<Transaction> findAll(@RequestParam(required = false) UUID accountId) {
        return accountId == null ? service.findAll() : service.findByAccount(accountId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
