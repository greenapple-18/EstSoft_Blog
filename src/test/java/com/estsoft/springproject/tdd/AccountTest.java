package com.estsoft.springproject.tdd;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    Account account;

    @BeforeEach
    public void setUp() {
        account = new Account(10000);
    }


    @Test
    public void testAccount() {
        assertThat(account.getBalance(), is(10000));

    }

    @Test
    public void testDeposit() {
        account.deposit(10000);
        assertThat(account.getBalance(), is(20000));
    }

    @Test
    public void testWithdraw() {
        account.withdraw(10000);
        assertThat(account.getBalance(), is(0));
    }
}
