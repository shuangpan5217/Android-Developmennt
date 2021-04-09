package edu.sjsu.android.mortgagecalculator;

public class CalculateLoanUtil {
    public static final int MONTHLY_DIVISOR = 1200;
    public static final float TAX_RATE = (float) 0.001;
    public static final int MONTH_IN_YEAR = 12;

    public static float calculate(float amount, float annalInterest, int term, boolean tax){
        float monthlyRate = annalInterest / MONTHLY_DIVISOR;
        int months = MONTH_IN_YEAR * term;
        float taxes = amount * TAX_RATE;

        //zero interest
        if(annalInterest == 0){
            if(tax){
                return amount / months + taxes;
            }else{
                return amount / months;
            }
        }

        float monthlyPayment = amount * (monthlyRate / (1 - (float) (Math.pow(1 + monthlyRate, -months))));
        if(tax){
            return monthlyPayment + taxes;
        }
        return monthlyPayment;
    }
}
