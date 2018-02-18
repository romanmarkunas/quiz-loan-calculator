package com.romanmarkunas.loan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class LoanCalculator {

    public static void main(String[] args) throws IOException {
        int loanAmount = Integer.parseInt(args[1]);
        List<Lender> lenderList = Files
                .readAllLines(Paths.get(args[0]))
                .stream()
                .skip(1)
                .map(csvRow -> {
                    String[] values = csvRow.split(",");
                    return new Lender(
                            Float.parseFloat(values[1]),
                            Integer.parseInt(values[2]));
                })
                .collect(toList());

        // TODO also add amount validation
        // TODO add tests for loan
        Optional<Loan> possibleLoan = Loan.cheapest(loanAmount, lenderList, 36);
        if (!possibleLoan.isPresent()) {
            System.out.println(String.format("Unable to lend %s", loanAmount));
            return;
        }

        Loan loan = possibleLoan.get();

        // TODO - calculate moneys.
        // simplest solution is to make repayments go into each lender
        // proportional to share, but that would not yield best rate
        // Most expensive loans must be repaid first. The challenge is to
        // normalize repayment in this conditions
    }
}
