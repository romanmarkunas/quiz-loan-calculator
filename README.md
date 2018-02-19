# Loan calculator
This program allows to calculate cheapest loan option from multiple
lenders.

### Usage
Requirements:
- Java version 8 or later

Checkout git repo:
```
git clone ...
cd quiz-loan-calculator
```

To run as part of Gradle build:

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
