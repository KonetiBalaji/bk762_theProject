package org.example;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportManager {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    public void exportToConsole(List<Expense> expenses) {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses list cannot be null");
        }

        if (expenses.isEmpty()) {
            System.out.println("No expenses to export.");
            return;
        }

        printHeader();
        printSeparator();
        expenses.forEach(this::printExpense);
    }

    private void printHeader() {
        System.out.printf("%-10s %-10s %-15s %-10s %-20s%n",
                "Expense ID", "User ID", "Category", "Amount", "Date");
    }

    private void printSeparator() {
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < 65; i++) {
            separator.append("-");
        }
        System.out.println(separator.toString());
    }

    private void printExpense(Expense expense) {
        System.out.printf("%-10s %-10s %-15s %10.2f %-20s%n",
                expense.getId(),
                expense.getUserId(),
                expense.getCategory(),
                expense.getAmount(),
                formatter.format(expense.getDate()));
    }
}
