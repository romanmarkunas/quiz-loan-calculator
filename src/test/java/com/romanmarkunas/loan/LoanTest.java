package com.romanmarkunas.loan;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created by Romans Markuns
 */
public class LoanTest {

    @Test
    public void getMonthlyRepayment_calculationCorrespondsToTheoretical() {
        Lender l1 = new Lender(0.05f, 1000);
        Lender l2 = new Lender(0.10f, 1000);
        List<Lender> lenders = asList(l1, l2);

        Loan testLoan = Loan.cheapest(2000, lenders, 20).get();

        assertEquals(105.6f, testLoan.getMonthlyRepayment(), 0.0000001f);
    }
}