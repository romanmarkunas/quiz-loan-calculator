package com.romanmarkunas.loan;

public class Lender implements Comparable<Lender> {

    private final float rate;
    private final int amount;


    public Lender(float rate, int amount) {
        this.rate = rate;
        this.amount = amount;
    }


    public float getRate() {
        return this.rate;
    }

    public int getAmount() {
        return this.amount;
    }


    @Override
    public int compareTo(Lender other) {
        float difference = this.rate - other.rate;
        if (Math.abs(difference) < 0.0001f) {
            return 0;
        }
        else if (difference > 0.0f) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
