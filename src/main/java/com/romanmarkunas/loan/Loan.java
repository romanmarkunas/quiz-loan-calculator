package com.romanmarkunas.loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Loan {

    private final List<Lender> lenders;
    private final int amount;


    private Loan(List<Lender> lenders, int amount) {
        this.lenders = lenders;
        this.amount = amount;
    }


    public List<Lender> getLenders() {
        return Collections.unmodifiableList(this.lenders);
    }

    public int getAmount() {
        return this.amount;
    }


    public static Optional<Loan> forAmount(int amount, List<Lender> lenders) {
        List<Lender> bestLendersForLoan = new ArrayList<>();
        Collections.sort(lenders);
        int total = 0;

        for (Lender lender : lenders) {
            int nextTotal = total + lender.getAmount();

            if (nextTotal > amount) {
                bestLendersForLoan.add(
                        new Lender(lender.getRate(), nextTotal - amount));
            }
            else {
                bestLendersForLoan.add(lender);
            }

            if (nextTotal >= amount) {
                return Optional.of(new Loan(bestLendersForLoan, amount));
            }
        }

        return Optional.empty();
    }
}
