package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    //maps the sql table
    private Account accountObjectMapper(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setBalance(results.getBigDecimal("balance"));

        return account;
    }

   //Views balance of current user
    @Override
    public BigDecimal getBalance(long userId) {

        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    //When a user Id is entered it will pull up user's account
    @Override
    public Account getAnAccountByUserId(long userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, userId);

        Account account = null;
        if (results.next()) {
            account = accountObjectMapper(results);
        }

        return account;
    }

    //Method to update user balance after receiving transfers
    @Override
    public void addBalance(BigDecimal amount, long userId) {

        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, userId);

    }

     //Method to update user balance after sending transfers
    @Override
    public boolean subtractBalance(BigDecimal amount, long userId) {
        Account account = getAnAccountByUserId(userId);
        int res = account.getBalance().compareTo(amount);

        if (res == 1 || res == 0) {
            String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
            jdbcTemplate.update(sql, amount, userId);

            return true;
        } else {
            return false;
        }


    }
}