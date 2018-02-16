package com.romanmarkunas.loan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoanCalculator {

    public static void main(String[] args) throws IOException {
        List<String> lenderRows = Files.readAllLines(Paths.get(args[0]));
        int loanAmount = Integer.parseInt(args[1]);
        lenderRows.forEach(System.out::println);
        System.out.println(loanAmount);
    }
}
