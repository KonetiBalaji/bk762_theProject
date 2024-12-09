package org.example;

import org.example.Expense;
import org.example.ExpenseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseManagerTest {

    private ExpenseManager expenseManager;

    @BeforeEach
    public void setup() {
        expenseManager = new ExpenseManager();
    }


    @Test
    public void testAddExpense() {
        // Arrange
        Expense expense = new Expense("1", "User1", "Category1",10.0, new Date());

        // Act
        expenseManager.addExpense(expense);

        // Assert
        assertEquals(1, expenseManager.getExpensesByUser("User1").size());
    }

    @Test
    public void testRemoveExpense() {
        // Arrange
        Expense expense = new Expense("1", "User1","Category1", 10.0,  new Date());
        expenseManager.addExpense(expense);

        // Act
        boolean result = expenseManager.removeExpense("1");

        // Assert
        assertTrue(result);
        assertEquals(0, expenseManager.getExpensesByUser("User1").size());
    }

    @Test
    public void testRemoveExpense_NotFound() {
        // Act
        boolean result = expenseManager.removeExpense("1");

        // Assert
        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("provideFilterByCategoryData")
    public void testFilterByCategory(String userId, String category, int expectedCount) {
        // Arrange
        Expense expense = new Expense("1", userId, "category",10.0, new Date());
        expenseManager.addExpense(expense);

        // Act
        List<Expense> result = expenseManager.filterByCategory(userId, category);

        // Assert
        assertEquals(expectedCount, result.size());
    }

    private static Stream<Arguments> provideFilterByCategoryData() {
        return Stream.of(
                Arguments.of("User1", "Category1", 0),
                Arguments.of("User2", "Category2", 0)
        );
    }

    @Test
    public void testFilterByCategory_NullInputs() {
        // Act
        List<Expense> result = expenseManager.filterByCategory(null, null);

        // Assert
        assertEquals(0, result.size());
    }

    @ParameterizedTest
    @MethodSource("provideDateRangeData")
    public void testFilterByDateRange(String userId, Date startDate, Date endDate, int expectedCount) {
        // Arrange
        Expense expense = new Expense("1", userId, "Category1",10.0,  new Date());

        expenseManager.addExpense(expense);

        // Act
        List<Expense> result = expenseManager.filterByDateRange(userId, startDate, endDate);

        // Assert
        assertEquals(expectedCount, result.size());
    }

    private static Stream<Arguments> provideDateRangeData() {
        return Stream.of(
                Arguments.of("User1", new Date(122, 0, 1), new Date(122, 0, 31), 0),
                Arguments.of("User2", new Date(122, 1, 1), new Date(122, 1, 28), 0)
        );
    }

    @Test
    public void testFilterByDateRange_NullInputs() {
        // Act
        List<Expense> result = expenseManager.filterByDateRange(null, null, null);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testSortExpensesByAmount() {
        // Arrange
        Expense expense1 = new Expense("1", "User1", "Category1", 10.0, new Date());
        Expense expense2 = new Expense("2", "User1", "Category2", 20.0, new Date());
        expenseManager.addExpense(expense1);
        expenseManager.addExpense(expense2);

        // Act
        List<Expense> result = expenseManager.sortExpensesByAmount("User1");

        // Assert
        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0).getAmount(), 0.01);
    }

    @Test
    public void testGetExpensesByUser_NullAndEmptyCase() {
        // Test null userId
        List<Expense> result = expenseManager.getExpensesByUser(null);
        assertTrue(result.isEmpty());

        // Test empty userId
        result = expenseManager.getExpensesByUser("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleExpensesForSameUser() {
        Date date = new Date();
        Expense expense1 = new Expense("1", "User1", "Food", 10.0, date);
        Expense expense2 = new Expense("2", "User1", "Travel", 20.0, date);
        Expense expense3 = new Expense("3", "User2", "Food", 30.0, date);

        expenseManager.addExpense(expense1);
        expenseManager.addExpense(expense2);
        expenseManager.addExpense(expense3);

        List<Expense> user1Expenses = expenseManager.getExpensesByUser("User1");
        assertEquals(2, user1Expenses.size());
        assertTrue(user1Expenses.contains(expense1));
        assertTrue(user1Expenses.contains(expense2));
    }

    @Test
    public void testSortExpensesByAmount_EmptyAndNullCases() {
        // Test with non-existent user
        List<Expense> result = expenseManager.sortExpensesByAmount("NonExistentUser");
        assertTrue(result.isEmpty());

        // Test with null user
        result = expenseManager.sortExpensesByAmount(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortExpensesByAmount_MultipleExpenses() {
        Date date = new Date();
        Expense expense1 = new Expense("1", "User1", "Food", 30.0, date);
        Expense expense2 = new Expense("2", "User1", "Travel", 10.0, date);
        Expense expense3 = new Expense("3", "User1", "Shopping", 20.0, date);

        expenseManager.addExpense(expense1);
        expenseManager.addExpense(expense2);
        expenseManager.addExpense(expense3);

        List<Expense> sortedExpenses = expenseManager.sortExpensesByAmount("User1");
        assertEquals(3, sortedExpenses.size());
        assertEquals(10.0, sortedExpenses.get(0).getAmount());
        assertEquals(20.0, sortedExpenses.get(1).getAmount());
        assertEquals(30.0, sortedExpenses.get(2).getAmount());
    }

    @Test
    public void testFilterByCategory_CaseInsensitive() {
        Date date = new Date();
        Expense expense1 = new Expense("1", "User1", "Food", 10.0, date);
        Expense expense2 = new Expense("2", "User1", "FOOD", 20.0, date);
        Expense expense3 = new Expense("3", "User1", "food", 30.0, date);

        expenseManager.addExpense(expense1);
        expenseManager.addExpense(expense2);
        expenseManager.addExpense(expense3);

        List<Expense> foodExpenses = expenseManager.filterByCategory("User1", "FoOd");
        assertEquals(3, foodExpenses.size());
    }

    @Test
    public void testFilterByDateRange_BoundaryConditions() {
        Date date = new Date();
        Expense expense = new Expense("1", "User1", "Food", 10.0, date);
        expenseManager.addExpense(expense);

        // Test exact same start and end date
        List<Expense> result = expenseManager.filterByDateRange("User1", date, date);
        assertEquals(1, result.size());

        // Test date range where expense date is exactly at start
        result = expenseManager.filterByDateRange("User1", date, new Date(date.getTime() + 1000));
        assertEquals(1, result.size());

        // Test date range where expense date is exactly at end
        result = expenseManager.filterByDateRange("User1", new Date(date.getTime() - 1000), date);
        assertEquals(1, result.size());
    }

}