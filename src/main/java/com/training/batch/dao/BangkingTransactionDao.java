package com.training.batch.dao;

import com.training.batch.Dto.BankingTransaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BangkingTransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public BangkingTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(BankingTransaction bankingTransaction) {
        String sql = "insert into banking_transaction values (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                bankingTransaction.getTransactionId()
        ,bankingTransaction.getAccountNumber()
        ,bankingTransaction.getCustomerName()
        ,bankingTransaction.getTransactionType()
        ,bankingTransaction.getAmount()
        ,bankingTransaction.getTransactionDate()
        ,bankingTransaction.getBranchCode()
        ,bankingTransaction.getDescription());
    }
}

