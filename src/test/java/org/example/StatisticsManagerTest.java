package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsManagerTest {
    private StatisticsManager statisticsManager;
    private List<Expense> expenses;

    @BeforeEach
    void setUp() {
        statisticsManager = new StatisticsManager();
        expenses = new ArrayList<>();
        expenses.add(new Expense("1", "123", "Food", 50.0, new Date()));
        expenses.add(new Expense("2", "123", "Travel", 150.0, new Date()));
    }

    @Test
    void testNullExpensesList() {
        assertThrows(IllegalArgumentException.class, () -> statisticsManager.calculateTotal(null));
        assertThrows(IllegalArgumentException.class, () -> statisticsManager.calculateMax(null));
        assertThrows(IllegalArgumentException.class, () -> statisticsManager.calculateAverage(null));
    }

    @Test
    void testEmptyExpenseList() {
        List<Expense> emptyExpenses = new ArrayList<>();
        assertEquals(0.0, statisticsManager.calculateTotal(emptyExpenses));
        assertEquals(0.0, statisticsManager.calculateMax(emptyExpenses));
        assertEquals(0.0, statisticsManager.calculateAverage(emptyExpenses));
    }

    @Test
    void testSingleExpense() {
        List<Expense> singleExpense = Arrays.asList(
                new Expense("1", "123", "Food", 50.0, new Date())
        );
        assertEquals(50.0, statisticsManager.calculateTotal(singleExpense));
        assertEquals(50.0, statisticsManager.calculateMax(singleExpense));
        assertEquals(50.0, statisticsManager.calculateAverage(singleExpense));
    }

    @Test
    void testPrecisionHandling() {
        List<Expense> precisionExpenses = Arrays.asList(
                new Expense("1", "123", "Food", 50.123, new Date()),
                new Expense("2", "123", "Travel", 150.456, new Date())
        );
        assertEquals(200.58, statisticsManager.calculateTotal(precisionExpenses));
        assertEquals(150.46, statisticsManager.calculateMax(precisionExpenses));
        assertEquals(100.29, statisticsManager.calculateAverage(precisionExpenses));
    }

    @ParameterizedTest
    @MethodSource("provideExpensesForCalculations")
    void testCalculations(List<Expense> testExpenses, double expectedTotal,
                          double expectedMax, double expectedAverage) {
        assertEquals(expectedTotal, statisticsManager.calculateTotal(testExpenses));
        assertEquals(expectedMax, statisticsManager.calculateMax(testExpenses));
        assertEquals(expectedAverage, statisticsManager.calculateAverage(testExpenses));
    }

    private static Stream<Object[]> provideExpensesForCalculations() {
        Date date = new Date();
        return Stream.of(
                new Object[]{
                        Arrays.asList(
                                new Expense("1", "123", "Food", 100.00, date),
                                new Expense("2", "123", "Travel", 200.00, date)
                        ),
                        300.00, 200.00, 150.00
                },
                new Object[]{
                        Arrays.asList(
                                new Expense("1", "123", "Food", 1.23, date),
                                new Expense("2", "123", "Travel", 4.56, date),
                                new Expense("3", "123", "Other", 7.89, date)
                        ),
                        13.68, 7.89, 4.56
                }
        );
    }
}
