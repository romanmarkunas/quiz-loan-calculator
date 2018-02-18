package com.romanmarkunas.loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Loan {

    private final List<Lender> lenders;
    private final int amount;
    private final int durationMonth;


    private Loan(List<Lender> lenders, int amount, int durationMonths) {
        this.lenders = lenders;
        this.lenders.sort(Collections.reverseOrder());
        this.amount = amount;
        this.durationMonth = durationMonths;
    }


    public List<Lender> getLenders() {
        return Collections.unmodifiableList(this.lenders);
    }

    public int getAmount() {
        return this.amount;
    }

    /**
     * This repayment assumes that for the benefit of customer, their's first
     * payments come toward to repay lender with highest rate. This gives
     * borrower better total rate.
     *
     * Implementation uses guess logic for simplicity. There is a way to solve
     * it using equation systems, but is much less readable and simple solution
     * with binary approximation gives good enough performance for test sake
     */
    public float getMonthlyRepayment() {
        float lessThanRepayment = this.amount / this.durationMonth;
        float moreThanRepayment = this.amount * (1 + this.lenders.get(0).getRate());
        float repayment;

        while (true) {
            float loanDuration = 0.0f;
            repayment = (moreThanRepayment + lessThanRepayment) / 2;

            float previousLendersRepaymentDurationMonths = 0.0f;
            for (Lender lender : this.lenders) {
                float thisLenderRepaymentsDuration = getLenderRepaymentDurationMonths(
                        repayment,
                        lender.getRate(),
                        lender.getAmount(),
                        previousLendersRepaymentDurationMonths);
                previousLendersRepaymentDurationMonths = thisLenderRepaymentsDuration;
                loanDuration += thisLenderRepaymentsDuration;
            }

            if (loanDuration < this.durationMonth) {
                moreThanRepayment = repayment;
            }
            else {
                lessThanRepayment = repayment;
            }

            if (Math.abs(loanDuration - this.durationMonth) < 0.001) {
                break;
            }
        }

        return repayment;
    }

    private float getLenderRepaymentDurationMonths(
            float repayment,
            float lenderAnnualRate,
            int principal,
            float lenderWaitBeforeRepaymentsStartMonths) {
        float lenderMonthlyRate = lenderAnnualRate / 12;
        double principalAfterWait = principal * Math.pow(
                1 + lenderMonthlyRate,
                lenderWaitBeforeRepaymentsStartMonths);
        double logBase = 1 + lenderMonthlyRate;
        double logArg = 1 / (1 - (principalAfterWait * lenderMonthlyRate / repayment));
        // in java we don't have arbitrary base logarithms so we'll solve this
        // using identity log^n(b) = log^e(n) / log^e(b)
        return (float)(Math.log(logArg) / Math.log(logBase));
    }

    public float getTotalRepayment() {
        return getMonthlyRepayment() * this.durationMonth;
    }


    public static Optional<Loan> cheapest(int amount, List<Lender> lenders, int months) {
        List<Lender> bestLendersForLoan = new ArrayList<>();
        Collections.sort(lenders);
        int total = 0;

        for (Lender lender : lenders) {
            total += lender.getAmount();

            if (total > amount) {
                bestLendersForLoan.add(
                        new Lender(lender.getRate(), total - amount));
            }
            else {
                bestLendersForLoan.add(lender);
            }

            if (total >= amount) {
                return Optional.of(new Loan(bestLendersForLoan, amount, months));
            }
        }

        return Optional.empty();
    }
}
