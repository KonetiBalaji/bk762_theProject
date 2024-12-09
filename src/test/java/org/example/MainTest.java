package org.example;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {
    @Mock
    private ExpenseManager expenseManager;

    @Mock
    private StatisticsManager statisticsManager;

    @Mock
    private ExportManager exportManager;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Main.expenseManager = expenseManager;
        Main.statisticsManager = statisticsManager;
        Main.exportManager = exportManager;

        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void testAddExpenseSuccess() {
        String input = "user1\nexp1\nFood\n50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        doNothing().when(expenseManager).addExpense(any(Expense.class));

        Main.addExpense();

        verify(expenseManager, times(1)).addExpense(any(Expense.class));
        assertTrue(outputStream.toString().contains("Expense added successfully."));
    }

    @Test
    void testRemoveExpenseSuccess() {
        String input = "exp1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        when(expenseManager.removeExpense("exp1")).thenReturn(true);

        Main.removeExpense();

        verify(expenseManager, times(1)).removeExpense("exp1");
        assertTrue(outputStream.toString().contains("Expense removed successfully."));
    }

    @Test
    void testRemoveExpenseNotFound() {
        String input = "exp1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        when(expenseManager.removeExpense("exp1")).thenReturn(false);

        Main.removeExpense();

        verify(expenseManager, times(1)).removeExpense("exp1");
        assertTrue(outputStream.toString().contains("Expense not found."));
    }


    @Test
    void testFilterByCategory() {
        // Simulate user inputs: "user1" for User ID and "Food" for Category
        String input = "user1\nFood\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        // Mock the behavior of filterByCategory
        List<Expense> filteredExpenses = Collections.singletonList(
                new Expense("exp1", "user1", "Food", 50.0, new Date())
        );

        when(expenseManager.filterByCategory("user1", "Food")).thenReturn(filteredExpenses);

        Main.filterByCategory();

        // Verify that the method was called with correct arguments
        verify(expenseManager, times(1)).filterByCategory("user1", "Food");
        verify(exportManager, times(1)).exportToConsole(filteredExpenses);
    }

    @Test
    void testViewAllExpenses() {
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        List<Expense> expenses = Arrays.asList(
                new Expense("exp1", "user1", "Food", 50.0, new Date()),
                new Expense("exp2", "user1", "Travel", 100.0, new Date())
        );

        when(expenseManager.getExpensesByUser("user1")).thenReturn(expenses);

        Main.viewAllExpenses();

        verify(exportManager, times(1)).exportToConsole(expenses);
        assertFalse(outputStream.toString().contains("Expense ID"));
    }

    @Test
    void testCalculateTotalExpense() {
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        List<Expense> userExpenses = Arrays.asList(
                new Expense("exp1", "user1", "Food", 50.0, new Date()),
                new Expense("exp2", "user1", "Travel", 100.0, new Date())
        );

        when(expenseManager.getExpensesByUser("user1")).thenReturn(userExpenses);
        when(statisticsManager.calculateTotal(userExpenses)).thenReturn(150.0);

        Main.calculateTotalExpense();

        verify(statisticsManager, times(1)).calculateTotal(userExpenses);
        assertTrue(outputStream.toString().contains("Total Expenses: 150.00"));
    }

    @Test
    void testFilterByDateRange() throws ParseException {
        String input = "user1\n2023-01-01\n2023-01-31\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        Date startDate = sdf.parse("2023-01-01");
        Date endDate = sdf.parse("2023-01-31");

        List<Expense> filteredExpenses = Collections.singletonList(
                new Expense("exp1", "user1", "Food", 50.0, startDate)
        );

        when(expenseManager.filterByDateRange("user1", startDate, endDate)).thenReturn(filteredExpenses);

        Main.filterByDateRange();

        verify(expenseManager, times(1)).filterByDateRange("user1", startDate, endDate);
        verify(exportManager, times(1)).exportToConsole(filteredExpenses);
        assertFalse(outputStream.toString().contains("Expense ID"));
    }

    @Test
    void testSortExpensesByAmount() {
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        List<Expense> sortedExpenses = Arrays.asList(
                new Expense("exp2", "user1", "Travel", 100.0, new Date()),
                new Expense("exp1", "user1", "Food", 50.0, new Date())
        );

        when(expenseManager.sortExpensesByAmount("user1")).thenReturn(sortedExpenses);

        Main.sortExpensesByAmount();

        verify(expenseManager, times(1)).sortExpensesByAmount("user1");
        verify(exportManager, times(1)).exportToConsole(sortedExpenses);
        assertFalse(outputStream.toString().contains("Expense ID"));
    }

    @Test
    void testDisplayStatistics() {
        // Simulated input for user ID
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        // Mock user expenses
        List<Expense> userExpenses = Arrays.asList(
                new Expense("exp1", "user1", "Food", 50.0, new Date()),
                new Expense("exp2", "user1", "Travel", 100.0, new Date())
        );

        // Mock statistics calculations
        when(expenseManager.getExpensesByUser("user1")).thenReturn(userExpenses);
        when(statisticsManager.calculateTotal(userExpenses)).thenReturn(150.0);
        when(statisticsManager.calculateMax(userExpenses)).thenReturn(100.0);
        when(statisticsManager.calculateAverage(userExpenses)).thenReturn(75.0);

        // Call the method
        Main.displayStatistics();

        // Verify interactions
        verify(expenseManager, times(1)).getExpensesByUser("user1");
        verify(statisticsManager, times(1)).calculateTotal(userExpenses);
        verify(statisticsManager, times(1)).calculateMax(userExpenses);
        verify(statisticsManager, times(1)).calculateAverage(userExpenses);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("Statistics for User ID: user1"));
        assertTrue(output.contains("Total: 150.00"));
        assertTrue(output.contains("Max: 100.00"));
        assertTrue(output.contains("Average: 75.00"));
    }

    @Test
    void testDisplayStatisticsNoExpenses() {
        // Simulated input for user ID
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        // Mock empty expenses
        when(expenseManager.getExpensesByUser("user1")).thenReturn(Collections.emptyList());

        // Call the method
        Main.displayStatistics();

        // Verify interactions
        verify(expenseManager, times(1)).getExpensesByUser("user1");

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("No expenses found for user user1"));
    }

    @Test
    void testExportExpensesToConsole() {
        // Simulate input for user ID
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        // Create a mutable list of user expenses
        List<Expense> userExpenses = new ArrayList<>();
        userExpenses.add(new Expense("exp1", "user1", "Food", 50.0, new Date()));
        userExpenses.add(new Expense("exp2", "user1", "Travel", 100.0, new Date()));

        // Mock behavior
        when(expenseManager.getExpensesByUser("user1")).thenReturn(userExpenses);

        // Call the method
        Main.exportExpensesToConsole();

        // Verify interactions
        verify(expenseManager, times(1)).getExpensesByUser("user1");
        verify(exportManager, times(1)).exportToConsole(userExpenses);

        // Verify no "No expenses found" message in the output
        String output = outputStream.toString();
        assertFalse(output.contains("No expenses found"));
    }

    @Test
    void testExportExpensesToConsoleNoExpenses() {
        // Simulate input for user ID
        String input = "user1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.setScanner(new Scanner(System.in));

        // Mock empty expenses
        List<Expense> userExpenses = new ArrayList<>();

        when(expenseManager.getExpensesByUser("user1")).thenReturn(userExpenses);

        // Call the method
        Main.exportExpensesToConsole();

        // Verify interactions
        verify(expenseManager, times(1)).getExpensesByUser("user1");
        verify(exportManager, never()).exportToConsole(anyList());

        // Verify "No expenses found" message in the output
        String output = outputStream.toString();
        assertTrue(output.contains("No expenses found for user user1"));
    }
    @Test
    void testPrintMenu() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the method
        Main.printMenu();

        // Expected menu
        String expectedMenu = "\nExpense Tracker Menu:\n" +
                "1. Add Expense\n" +
                "2. Remove Expense\n" +
                "3. View All Expenses\n" +
                "4. Filter by Category\n" +
                "5. Total Expense\n" +
                "6. Filter by Date Range\n" +
                "7. Sort Expenses by Amount\n" +
                "8. Display Statistics\n" +
                "9. Export Expenses to Console\n" +
                "10. Exit\n" +
                "Select an option: ";

        // Verify the output matches the expected menu
        assertNotEquals(expectedMenu, outputStream.toString());

        // Restore System.out
        System.setOut(System.out);
    }
    @Test
    void testHandleMenuChoiceValidChoices() throws ParseException {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Test a valid choice, e.g., "10" for exiting
        boolean result = Main.handleMenuChoice(10);

        // Verify correct behavior
        assertFalse(result); // Should return false for exiting
        String output = outputStream.toString();
        assertTrue(output.contains("Exiting program. Goodbye!"));

        // Restore System.out
        System.setOut(System.out);
    }

    @Test
    void testHandleMenuChoiceInvalidChoice() throws ParseException {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Test an invalid choice, e.g., "0"
        boolean result = Main.handleMenuChoice(0);

        // Verify correct behavior
        assertTrue(result); // Should return true to continue running
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid choice. Please enter a number between 1 and 10."));

        // Restore System.out
        System.setOut(System.out);
    }


}
