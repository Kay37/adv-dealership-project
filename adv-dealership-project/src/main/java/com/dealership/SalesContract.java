package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;

//! adds: tax, fees, finance yes/no
public class SalesContract extends Contract {

    //! constants, the same for every SalesContract
    private static final BigDecimal SALES_TAX_RATE = new BigDecimal("0.05");
    private static final BigDecimal RECORDING_FEE = new BigDecimal("100.00");
    private static final double FOUR_YEAR_RATE = 0.0425;
    private static final double TWO_YEAR_RATE = 0.0525;
    private static final int FOUR_YEARS_AS_MONTHS = 48;
    private static final int TWO_YEARS_AS_MONTHS = 24;

    //! fields/variables that can be used when constructing a SalesContract
    private boolean isFinanced; //! the buyer chooses if this is true or false
    private BigDecimal processingFee = BigDecimal.ZERO;
    private BigDecimal salesTaxAmount = BigDecimal.ZERO;

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicleSold); //! parent constructor fills in what every type of contract has
        this.isFinanced = isFinanced; //! placing our fields unique to this sales contract into the constructor
        //! so that they persist for every Sales contract object

        //! BigDecimal math, rounded to cents
        this.salesTaxAmount = getVehicleSold().getPrice().multiply(SALES_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        //! if the vehicle sold in this contract's price is less than 10,000 (compareTo will return -1, 0, or 1 and
        //! if it returns a negative number then we know the vehicle's price is less than 10k) then the Fee is $295
        if (getVehicleSold().getPrice().compareTo(BigDecimal.valueOf(10000)) < 0) {
            this.processingFee = new BigDecimal("295.00");
        } else {
            //! otherwise Fee is $495
            this.processingFee = new BigDecimal("495.00");
        }
    }

    //! getters and setters
    public boolean isFinanced() {
        return isFinanced;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public BigDecimal getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public BigDecimal getRecordingFee() {
        return RECORDING_FEE;
    }

    public void setFinanced(boolean financed) {
        isFinanced = financed;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public void setSalesTaxAmount(BigDecimal salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return getVehicleSold().getPrice().add(salesTaxAmount).add(RECORDING_FEE).add(processingFee);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        BigDecimal monthlyPayment = BigDecimal.ZERO;
        double rate;
        int months;
        if (!isFinanced) {
            monthlyPayment = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP);
        } else {
            if (getTotalPrice().compareTo(BigDecimal.valueOf(10000)) < 0) {
                rate = TWO_YEAR_RATE;
                months = TWO_YEARS_AS_MONTHS;
            } else {
                //! 4.25% a year for 48 months
                rate = FOUR_YEAR_RATE;
                months = FOUR_YEARS_AS_MONTHS;
            }
            //! 5.25% a year for 24 months
            //!                      P × r
            //!monthly payment = ───────────────
            //                  1 − (1 + r)^−n
            //! P = the amount borrowed. Use the total price; the workshop’s lease example does it this way.
            //!r = the monthly rate, which is the yearly rate ÷ 12. For example, 4.25% → 0.0425 ÷ 12.
            //!n = the number of months (48 or 24).
            double p = getTotalPrice().doubleValue();
            double r = rate / 12;
            double n = months;
            monthlyPayment = BigDecimal.valueOf(((p * r) / (1 - Math.pow(1 + r, -n)))).setScale(2, RoundingMode.HALF_UP);
        }
        return monthlyPayment;
    }
}
