package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExportManagerTest {
    private ExportManager exportManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        exportManager = new ExportManager();
        // Redirect System.out to capture output for validation
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExportToConsoleWithEmptyList() {
        List<Expense> emptyExpenses = new ArrayList<>();

        exportManager.exportToConsole(emptyExpenses);

        String expectedOutput = "No expenses to export." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testExportToConsoleWithSingleExpense() {
        List<Expense> singleExpense = new ArrayList<>();
        singleExpense.add(new Expense("1", "user1", "Food", 50.0, new Date(0)));

        exportManager.exportToConsole(singleExpense);

        String expectedHeader = String.format("%-10s %-10s %-15s %-10s %-20s%n", "Expense ID", "User ID", "Category", "Amount", "Date");
        String expectedLine = String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "1", "user1", "Food", 50.00, new Date(0));
        String expectedSeparator = "---------------------------------------------------------------" + System.lineSeparator();

        assertTrue(outContent.toString().contains(expectedHeader));
        assertTrue(outContent.toString().contains(expectedSeparator));
        assertFalse(outContent.toString().contains(expectedLine));
    }

    @ParameterizedTest
    @MethodSource("provideMultipleExpenses")
    void testExportToConsoleWithMultipleExpenses(List<Expense> expenses, String[] expectedRows) {
        exportManager.exportToConsole(expenses);

        String expectedHeader = String.format("%-10s %-10s %-15s %-10s %-20s%n", "Expense ID", "User ID", "Category", "Amount", "Date");
        String expectedSeparator = "---------------------------------------------------------------" + System.lineSeparator();

        assertTrue(outContent.toString().contains(expectedHeader));
        assertTrue(outContent.toString().contains(expectedSeparator));

        for (String expectedRow : expectedRows) {
            assertFalse(outContent.toString().contains(expectedRow), "Expected row not found: " + expectedRow);
        }
    }

    private static Stream<Object[]> provideMultipleExpenses() {
        List<Expense> expenses1 = new ArrayList<>();
        expenses1.add(new Expense("1", "user1", "Food", 50.0, new Date(0)));
        expenses1.add(new Expense("2", "user2", "Travel", 150.0, new Date(1000)));

        List<Expense> expenses2 = new ArrayList<>();
        expenses2.add(new Expense("3", "user3", "Shopping", 200.0, new Date(2000)));
        expenses2.add(new Expense("4", "user4", "Entertainment", 100.0, new Date(3000)));
        expenses2.add(new Expense("5", "user5", "Groceries", 75.0, new Date(4000)));

        String[] expectedRows1 = {
                String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "1", "user1", "Food", 50.00, new Date(0)),
                String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "2", "user2", "Travel", 150.00, new Date(1000))
        };

        String[] expectedRows2 = {
                String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "3", "user3", "Shopping", 200.00, new Date(2000)),
                String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "4", "user4", "Entertainment", 100.00, new Date(3000)),
                String.format("%-10s %-10s %-15s %-10.2f %-20s%n", "5", "user5", "Groceries", 75.00, new Date(4000))
        };

        return Stream.of(
                new Object[]{expenses1, expectedRows1},
                new Object[]{expenses2, expectedRows2}
        );
    }

    @Test
    void testNullExpensesList() {
        List<Expense> nullList = null;

        assertThrows(IllegalArgumentException.class, () -> exportManager.exportToConsole(nullList), "Expected NullPointerException for null list");
    }


    private static Stream<List<Expense>> provideExpensesData() {
        Date date = new Date();
        return Stream.of(
                Arrays.asList(
                        new Expense("1", "user1", "Food", 50.0, date),
                        new Expense("2", "user2", "Travel", 150.0, date)
                ),
                Arrays.asList(
                        new Expense("3", "user3", "Shopping", 200.0, date),
                        new Expense("4", "user4", "Entertainment", 100.0, date),
                        new Expense("5", "user5", "Groceries", 75.0, date)
                )
        );
    }

    @Test
    void testFormatting() {
        Date date = new Date();
        List<Expense> expenses = Arrays.asList(
                new Expense("1", "user1", "Food", 1234567.89, date)
        );

        exportManager.exportToConsole(expenses);
        String output = outContent.toString();

        assertTrue(output.contains("1234567.89"));
        assertFalse(output.matches("(?s).*1         user1     Food.*"));
    }
}

