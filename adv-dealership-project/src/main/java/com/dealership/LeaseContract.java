package com.dealership;

import java.math.BigDecimal;
import java.math.RoundingMode;

//! adds: ending value, lease fee
public class LeaseContract extends Contract {

    private static final BigDecimal ENDING_VALUE_RATE = new BigDecimal("0.50");
    private static final BigDecimal LEASE_FEE_RATE = new BigDecimal("0.07");
    private static final double LEASE_RATE = 0.04;
    private static final int LEASE_MONTHS = 36;

    private BigDecimal leaseFeeAmount;
    private BigDecimal endingValueAmount;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        this.endingValueAmount = getVehicleSold().getPrice().multiply(ENDING_VALUE_RATE).setScale(2, RoundingMode.HALF_UP);
        this.leaseFeeAmount = getVehicleSold().getPrice().multiply(LEASE_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLeaseFeeAmount() {
        return leaseFeeAmount;
    }

    public BigDecimal getEndingValueAmount() {
        return endingValueAmount;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return endingValueAmount.add(leaseFeeAmount);
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        BigDecimal monthlyPayment = BigDecimal.ZERO;
        //! 4% rate for 36 months (3 years)
        //!                      P × r
        //!monthly payment = ───────────────
        //                  1 − (1 + r)^−n
        //! P = the amount borrowed. Use the total price; the workshop’s lease example does it this way.
        //!r = the monthly rate, which is the yearly rate ÷ 12. For example, 4.25% → 0.0425 ÷ 12.
        //!n = the number of months (48 or 24).
        double p = getTotalPrice().doubleValue();
        double r = LEASE_RATE / 12;
        double n = LEASE_MONTHS;
        monthlyPayment = BigDecimal.valueOf(((p * r) / (1 - Math.pow(1 + r, -n)))).setScale(2, RoundingMode.HALF_UP);
        return monthlyPayment;
    }
}
