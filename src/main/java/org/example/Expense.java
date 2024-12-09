package org.example;

import java.util.Date;
import java.util.Objects;

public class Expense {
    private final String id;
    private final String userId;
    private final String category;
    private final double amount;
    private final Date date;

    public Expense(String id, String userId, String category, double amount, Date date) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Expense ID cannot be null or empty");
        if (userId == null || userId.trim().isEmpty()) throw new IllegalArgumentException("User ID cannot be null or empty");
        if (category == null || category.trim().isEmpty()) throw new IllegalArgumentException("Category cannot be null or empty");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (date == null) throw new IllegalArgumentException("Date cannot be null");

        this.id = id.trim();
        this.userId = userId.trim();
        this.category = category.trim();
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.date = new Date(date.getTime());
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(expense.amount, amount) == 0 &&
                Objects.equals(id, expense.id) &&
                Objects.equals(userId, expense.userId) &&
                Objects.equals(category, expense.category) &&
                Objects.equals(date, expense.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, category, amount, date);
    }

    @Override
    public String toString() {
        return String.format("Expense{id='%s', userId='%s', category='%s', amount=%.2f, date=%s}",
                id, userId, category, amount, date);
    }
}