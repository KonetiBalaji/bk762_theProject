package org.example;

import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatisticsManager {
    private static final int DECIMAL_PLACES = 2;

    public double calculateTotal(List<Expense> expenses) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
        BigDecimal total = expenses.stream()
                .map(e -> BigDecimal.valueOf(e.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP).doubleValue();
    }

    public double calculateMax(List<Expense> expenses) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .max()
                .orElse(0.0);
    }

    public double calculateAverage(List<Expense> expenses) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }
        if (expenses.isEmpty()) {
            return 0.0;
        }
        BigDecimal total = BigDecimal.valueOf(calculateTotal(expenses));
        BigDecimal count = BigDecimal.valueOf(expenses.size());
        return total.divide(count, DECIMAL_PLACES, RoundingMode.HALF_UP).doubleValue();
    }
}