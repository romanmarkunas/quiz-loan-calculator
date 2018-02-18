# Loan calculator
This program allows to calculate cheapest loan option from multiple
lenders.

### Usage
As a part of Gradle build (if you don't have JRE on your machine):

```
./gradlew run -Pfile=path/to/lender/file.csv -Pamount=1000
```

As standalone jar:

```
./gradlew clean jar
java -jar build/libs/quiz-loan-calculator.jar path/to/lender/file.csv 4000
```

### Implementation notes
Since interest rate on loans is compound interest calculations
assume that borrower will repay more expensive lenders first, in
order to save.

Repayments and rate calculation do not use Newton's method or
equation systems for sake of code simplicity and readability.
