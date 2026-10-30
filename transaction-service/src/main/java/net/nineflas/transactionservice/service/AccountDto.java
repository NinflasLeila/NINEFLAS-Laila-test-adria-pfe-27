package net.nineflas.transactionservice.service;



import java.util.UUID;
public record AccountDto(UUID id, String customerReference, String status,
                         String currency, double balance) {}
