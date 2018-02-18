package com.romanmarkunas.loan;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanTest {

    @Test
    public void cheapest_prioritizesCheapestLenders() {
        Lender cheap1 = lenderMock(0.01f, 100);
        Lender cheap2 = lenderMock(0.02f, 100);
        Lender expensive1 = lenderMock(0.10f, 100);
        Lender expensive2 = lenderMock(0.11f, 100);
        List<Lender> lenders = asList(expensive1, cheap2, expensive2, cheap1);

        Loan testLoan = Loan.cheapest(200, lenders, 36).get();
        List<Lender> testLoanLenders = testLoan.getLenders();

        assertThat(testLoanLenders, containsInAnyOrder(cheap1, cheap2));
        assertThat(testLoanLenders,
                not(containsInAnyOrder(expensive1, expensive2)));
    }

    @Test
    public void cheapest_cutsLastMostExpensiveLenderToMeetAmount() {
        Lender l1 = lenderMock(0.01f, 100);
        Lender l2 = lenderMock(0.02f, 100);
        List<Lender> lenders = asList(l1, l2);

        Loan testLoan = Loan.cheapest(150, lenders, 10).get();
        List<Lender> testLoanLenders = testLoan
                .getLenders()
                .stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        assertEquals(50, testLoanLenders.get(0).getAmount());
    }

    @Test
    public void cheapest_emptyOptional_lendersDoesNotHaveEnoughMoney() {
        Lender l1 = lenderMock(0.01f, 1000);
        Lender l2 = lenderMock(0.01f, 1000);
        List<Lender> lenders = asList(l1, l2);

        Optional<Loan> loanOptional = Loan.cheapest(2001, lenders, 12);

        assertFalse(loanOptional.isPresent());
    }

    @Test
    public void getAmount_returnsSameAsPassedToBuilder() {
        int amount = 37649;
        Lender l = lenderMock(0.0f, 1_000_000);

        Loan testLoan = Loan.cheapest(amount, asList(l), 12).get();

        assertEquals(amount, testLoan.getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cheapest_throws_amountIsNegative() {
        Loan.cheapest(-100, emptyList(), 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cheapest_throws_monthsIsNegative() {
        Loan.cheapest(100, emptyList(), -12);
    }

    @Test(expected = NullPointerException.class)
    public void cheapest_throws_lendersListIsNull() {
        Loan.cheapest(100, null, 12);
    }

    @Test
    public void getMonthlyRepayment_calculationCorrespondsToTheoretical() {
        Lender l1 = lenderMock(0.05f, 1000);
        Lender l2 = lenderMock(0.10f, 1000);
        List<Lender> lenders = asList(l1, l2);

        Loan testLoan = Loan.cheapest(2000, lenders, 20).get();

        assertEquals(105.616f, testLoan.getMonthlyRepayment(), 0.001f);
    }


    private Lender lenderMock(float rate, int amount) {
        Lender mock = mock(Lender.class);
        when(mock.getAmount()).thenReturn(amount);
        when(mock.getRate()).thenReturn(rate);
        when(mock.compareTo(any())).thenCallRealMethod();
        return mock;
    }
}