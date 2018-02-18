package com.romanmarkunas.loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Loan {

    private final List<Lender> lenders;
    private final int amount;
    private final int durationMonth;
    private float monthlyRepayment = Float.NaN;


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
     * This repayment assumes that, for the benefit of customer, their's first
     * payments come toward to repay lender with highest rate. This gives
     * borrower better total rate.
     *
     * Implementation uses guess logic for simplicity. There is a way to solve
     * it using equation systems, but is less readable and simple solution
     * with binary approximation gives good enough performance for test sake.
     * For more on calculation specifics, see {@link #repaymentDurationMonths}
     * javadoc
     */
    public float getMonthlyRepayment() {
        if (Float.isNaN(this.monthlyRepayment)) {
            float notRounded = guessMonthlyRepayment();
            this.monthlyRepayment = Math.round(notRounded * 100) / 100.0f;
        }
        return this.monthlyRepayment;
    }

    public float getTotalRepayment() {
        return getMonthlyRepayment() * this.durationMonth;
    }


    public static Optional<Loan> cheapest(
            int amount,
            List<Lender> lenders,
            int months) {
        validate(amount, lenders, months);
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


    private static void validate(int amount, List<Lender> lenders, int months) {
        notNegative(amount, "Loan amount");
        notNegative(months, "Loan duration");
        checkNotNull(lenders, "Lender list must not be null!");
    }

    private static void notNegative(int arg, String name) {
        checkArgument(
                arg >= 0,
                String.format("%s must not be negative!", name));
    }

    private float guessMonthlyRepayment() {
        float repayment;
        float lessThanRepayment = this.amount / this.durationMonth;
        float moreThanRepayment =
                this.amount * (1 + this.lenders.get(0).getRate());

        while (true) {
            float loanDuration = 0.0f;
            repayment = (moreThanRepayment + lessThanRepayment) / 2;

            float previousLendersRepaymentDuration = 0.0f;
            for (Lender lender : this.lenders) {
                float thisLenderRepaymentsDuration = repaymentDurationMonths(
                        repayment,
                        lender.getRate(),
                        lender.getAmount(),
                        previousLendersRepaymentDuration);
                loanDuration += thisLenderRepaymentsDuration;
                previousLendersRepaymentDuration = thisLenderRepaymentsDuration;
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

    /**
     * Repayment duration calculation is based on loan repayment formula (mo is
     * the shorthand for "months"):
     * <pre>
     * {@code
     *              rate_mo * principal * (1 + rate_mo)^repayment_start_wait_mo
     * payment_mo = -----------------------------------------------------------
     *                      1 - (1 + rate_mo)^repayment_duration_mo
     * }
     * </pre>
     * So the function below is just inverse of this repayment function:
     * <pre>
     * {@code
     *                                                          1
     * repayment_duration_mo = log             (-------------------------------)
     *                            (1 + rate_mo)    rate_mo*principal_after_wait
     *                                          1-(----------------------------)
     *                                                      payment_mo
     * }
     * </pre>
     */
    private float repaymentDurationMonths(
            float payment,
            float annualRate,
            int principal,
            float monthsBeforePaymentsStart) {
        float monthlyRate = annualRate / 12;
        double principalAfterWait = principal * Math.pow(
                1 + monthlyRate,
                monthsBeforePaymentsStart);
        double logBase = 1 + monthlyRate;
        double logArg = 1 / (1 - (principalAfterWait * monthlyRate / payment));
        // in java we don't have arbitrary base logarithms so we'll solve this
        // using identity log^n(b) = log^e(n) / log^e(b)
        return (float)(Math.log(logArg) / Math.log(logBase));
    }
}
