package org.example;

import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    @Test
    void testValidExpenseCreation() {
        Date date = new Date();
        Expense expense = new Expense("1", "123", "Food", 100.0, date);

        assertEquals("1", expense.getId());
        assertEquals("123", expense.getUserId());
        assertEquals("Food", expense.getCategory());
        assertEquals(100.0, expense.getAmount());
        assertNotSame(date, expense.getDate());
    }

    @Test
    void testConstructorValidation() {
        Date date = new Date();

        assertThrows(IllegalArgumentException.class, () -> new Expense(null, "123", "Food", 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("", "123", "Food", 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("  ", "123", "Food", 100.0, date));

        assertThrows(IllegalArgumentException.class, () -> new Expense("1", null, "Food", 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "", "Food", 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "  ", "Food", 100.0, date));

        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", null, 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", "", 100.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", "  ", 100.0, date));

        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", "Food", 0.0, date));
        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", "Food", -50.0, date));

        assertThrows(IllegalArgumentException.class, () -> new Expense("1", "123", "Food", 100.0, null));
    }

    @Test
    void testDateImmutability() {
        Date originalDate = new Date();
        Expense expense = new Expense("1", "123", "Food", 100.0, originalDate);

        originalDate.setTime(originalDate.getTime() + 1000);
        assertNotEquals(originalDate, expense.getDate());

        Date retrievedDate = expense.getDate();
        retrievedDate.setTime(retrievedDate.getTime() + 1000);
        assertNotEquals(retrievedDate, expense.getDate());
    }

    @Test
    void testAmountPrecision() {
        Date date = new Date();
        Expense expense = new Expense("1", "123", "Food", 100.123, date);
        assertEquals(100.12, expense.getAmount(), 0.001);

        expense = new Expense("1", "123", "Food", 99.999, date);
        assertEquals(100.00, expense.getAmount(), 0.001);
    }

    @Test
    void testEqualsAndHashCode() {
        Date date = new Date();
        Expense expense1 = new Expense("1", "123", "Food", 100.0, date);
        Expense expense2 = new Expense("1", "123", "Food", 100.0, date);
        Expense expense3 = new Expense("2", "123", "Food", 100.0, date);

        assertTrue(expense1.equals(expense1));
        assertTrue(expense1.equals(expense2));
        assertTrue(expense2.equals(expense1));
        assertFalse(expense1.equals(expense3));
        assertFalse(expense1.equals(null));
        assertFalse(expense1.equals(new Object()));

        assertEquals(expense1.hashCode(), expense2.hashCode());
        assertNotEquals(expense1.hashCode(), expense3.hashCode());
    }

    @Test
    void testToStringFormat() {
        Date date = new Date();
        Expense expense = new Expense("1", "123", "Travel", 200.0, date);

        String expected = String.format("Expense{id='1', userId='123', category='Travel', amount=200.00, date=%s}", date);
        assertEquals(expected, expense.toString());
    }
}