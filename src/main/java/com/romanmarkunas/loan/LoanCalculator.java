package com.romanmarkunas.loan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
                .sorted()
                .collect(toList());

        lenderList.forEach(l -> System.out.println(l.getRate()));
    }
}
