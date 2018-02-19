package com.romanmarkunas.loan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

public class LoanCalculator {

    public static void main(String[] args) throws IOException {
        List<Lender> lenderList = readCsv(Paths.get(args[0]));
        int loanAmount = Integer.parseInt(args[1]);

        validateAmount(loanAmount);

        Optional<Loan> possibleLoan = Loan.cheapest(loanAmount, lenderList, 36);
        if (!possibleLoan.isPresent()) {
            System.out.println(String.format("Unable to lend %s", loanAmount));
            return;
        }

        outputLoan(possibleLoan.get());
    }

    private static void validateAmount(int amount) {
        checkArgument(amount >= 1000, "Loan amount must be £1000 or greater!");
        checkArgument(amount <= 15000, "Loan amount must be £15000 or less!");
        checkArgument(amount % 100 == 0, "Loan amount must be increment of 100");
    }

    private static List<Lender> readCsv(Path file) throws IOException {
        return Files
                .readAllLines(file)
                .stream()
                .skip(1)
                .map(csvRow -> {
                    String[] values = csvRow.split(",");
                    return new Lender(
                            Float.parseFloat(values[1]),
                            Integer.parseInt(values[2]));
                })
                .collect(toList());
    }

    private static void outputLoan(Loan loan) {
        System.out.println(String.format(
                "Requested amount: £%d \n" +
                "Rate: %.1f%%\n" +
                "Monthly repayment: £%.2f\n" +
                "Total repayment: £%.2f",
                loan.getAmount(),
                loan.getRate() * 100,
                loan.getMonthlyRepayment(),
                loan.getTotalRepayment()));
    }
}
