package com.dealership;

import java.math.BigDecimal;

//! adds: tax, fees, finance yes/no
public class SalesContract extends Contract {

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicleSold);
        isFinanced = isFinanced;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return null;
    }

    @Override
    public BigDecimal getMonthlyPayment() {
        return null;
    }
}
