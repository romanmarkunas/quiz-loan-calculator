package com.romanmarkunas.loan;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenderTest {

    @Test
    public void getRate() {
        float expectedRate = 0.4f;
        Lender testLender = new Lender(expectedRate, 0);
        assertEquals(expectedRate, testLender.getRate(), 0.001);
    }

    @Test
    public void getAmount() {
        int expectedAmount = 300;
        Lender testLender = new Lender(0.0f, expectedAmount);
        assertEquals(expectedAmount, testLender.getAmount());
    }

    @Test
    public void compare_positive_comparableLenderHasHigherRate() {
        Lender comparableLender = new Lender(0.4f, 100);
        Lender referenceLender = new Lender(0.3f, 1000);
        assertThat(comparableLender, Matchers.greaterThan(referenceLender));
    }

    @Test
    public void compare_negative_comparableLenderHasLowerRate() {
        Lender comparableLender = new Lender(0.2f, 100);
        Lender referenceLender = new Lender(0.3f, 1000);
        assertThat(comparableLender, Matchers.lessThan(referenceLender));
    }

    @Test
    public void compare_zero_lendersHaveSimilarRates() {
        Lender comparableLender = new Lender(0.3000001f, 100);
        Lender referenceLender = new Lender(0.3f, 1000);
        assertTrue(comparableLender.compareTo(referenceLender) == 0);
    }
}