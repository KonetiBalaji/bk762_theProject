package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class ExpenseManager {
    private final List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        expenses.add(expense);
    }

    public boolean removeExpense(String expenseId) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            return false;
        }
        return expenses.removeIf(expense -> expense.getId().equals(expenseId));
    }

    public List<Expense> getExpensesByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return expenses.stream()
                .filter(expense -> expense.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Expense> filterByCategory(String userId, String category) {
        if (userId == null || category == null) {
            return new ArrayList<>();
        }
        return getExpensesByUser(userId).stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Expense> filterByDateRange(String userId, Date startDate, Date endDate) {
        if (userId == null || startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        return getExpensesByUser(userId).stream()
                .filter(expense -> !expense.getDate().before(startDate) &&
                        !expense.getDate().after(endDate))
                .collect(Collectors.toList());
    }

    public List<Expense> sortExpensesByAmount(String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Expense> userExpenses = getExpensesByUser(userId);
        userExpenses.sort(Comparator.comparingDouble(Expense::getAmount));
        return userExpenses;
    }
}
