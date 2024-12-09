
package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in); // Default Scanner
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static org.example.ExpenseManager expenseManager = new ExpenseManager();
    static org.example.StatisticsManager statisticsManager = new StatisticsManager();
    static ExportManager exportManager = new ExportManager();

    public static void setScanner(Scanner customScanner) {
        scanner = customScanner; // Set custom Scanner for tests
    }

    public static void main(String[] args) {
        try {
            boolean continueRunning = true;
            while (continueRunning) {
                printMenu();
                try {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.println("Please enter a valid option.");
                        continue;
                    }
                    int choice = Integer.parseInt(input);
                    continueRunning = handleMenuChoice(choice);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                } catch (ParseException e) {
                    System.out.println("Error parsing date: " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static String validateStringInput(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(errorMessage);
                continue;
            }
            return input;
        }
    }

    private static double validateAmountInput() {
        while (true) {
            try {
                System.out.print("Enter Amount: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Amount cannot be empty");
                    continue;
                }
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("Amount must be greater than 0");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a valid number.");
            }
        }
    }

    private static Date validateDateInput(String prompt) throws ParseException {
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine().trim();
            if (dateStr.isEmpty()) {
                System.out.println("Date cannot be empty");
                continue;
            }
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd");
                throw e;
            }
        }
    }

    static void printMenu() {
        System.out.println("\nExpense Tracker Menu:");
        System.out.println("1. Add Expense");
        System.out.println("2. Remove Expense");
        System.out.println("3. View All Expenses");
        System.out.println("4. Filter by Category");
        System.out.println("5. Total Expense");
        System.out.println("6. Filter by Date Range");
        System.out.println("7. Sort Expenses by Amount");
        System.out.println("8. Display Statistics");
        System.out.println("9. Export Expenses to Console");
        System.out.println("10. Exit");
        System.out.print("Select an option: ");
    }

    static boolean handleMenuChoice(int choice) throws ParseException {
        switch (choice) {
            case 1: addExpense(); break;
            case 2: removeExpense(); break;
            case 3: viewAllExpenses(); break;
            case 4: filterByCategory(); break;
            case 5: calculateTotalExpense(); break;
            case 6: filterByDateRange(); break;
            case 7: sortExpensesByAmount(); break;
            case 8: displayStatistics(); break;
            case 9: exportExpensesToConsole(); break;
            case 10:
                System.out.println("Exiting program. Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 10.");
        }
        return true;
    }

    static void addExpense() {
        try {
            String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
            String expenseId = validateStringInput("Enter Expense ID: ", "Expense ID cannot be empty");
            String category = validateStringInput("Enter Category: ", "Category cannot be empty");
            double amount = validateAmountInput();

            expenseManager.addExpense(new Expense(expenseId, userId, category, amount, new Date()));
            System.out.println("Expense added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }

    static void removeExpense() {
        String expenseId = validateStringInput("Enter Expense ID: ", "Expense ID cannot be empty");
        if (expenseManager.removeExpense(expenseId)) {
            System.out.println("Expense removed successfully.");
        } else {
            System.out.println("Expense not found.");
        }
    }

    static void viewAllExpenses() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        List<Expense> expenses = expenseManager.getExpensesByUser(userId);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found for user " + userId);
            return;
        }
        exportManager.exportToConsole(expenses);
    }

    public static void filterByCategory() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        String category = validateStringInput("Enter Category: ", "Category cannot be empty");

        List<Expense> filteredExpenses = expenseManager.filterByCategory(userId, category);
        if (filteredExpenses.isEmpty()) {
            System.out.println("No expenses found in category: " + category);
            return;
        }
        exportManager.exportToConsole(filteredExpenses);
    }

    static void calculateTotalExpense() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        List<Expense> userExpenses = expenseManager.getExpensesByUser(userId);
        if (userExpenses.isEmpty()) {
            System.out.println("No expenses found for user " + userId);
            return;
        }
        double total = statisticsManager.calculateTotal(userExpenses);
        System.out.printf("Total Expenses: %.2f%n", total);
    }

    static void filterByDateRange() throws ParseException {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        Date startDate = validateDateInput("Enter Start Date (yyyy-MM-dd): ");
        Date endDate = validateDateInput("Enter End Date (yyyy-MM-dd): ");

        if (endDate.before(startDate)) {
            System.out.println("End date must be after start date");
            return;
        }

        List<Expense> filteredExpenses = expenseManager.filterByDateRange(userId, startDate, endDate);
        if (filteredExpenses.isEmpty()) {
            System.out.println("No expenses found in the given date range");
            return;
        }
        exportManager.exportToConsole(filteredExpenses);
    }

    static void sortExpensesByAmount() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        List<Expense> sortedExpenses = expenseManager.sortExpensesByAmount(userId);
        if (sortedExpenses.isEmpty()) {
            System.out.println("No expenses found for user " + userId);
            return;
        }
        exportManager.exportToConsole(sortedExpenses);
    }

    static void displayStatistics() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        List<Expense> userExpenses = expenseManager.getExpensesByUser(userId);

        if (userExpenses.isEmpty()) {
            System.out.println("No expenses found for user " + userId);
            return;
        }

        double total = statisticsManager.calculateTotal(userExpenses);
        double max = statisticsManager.calculateMax(userExpenses);
        double average = statisticsManager.calculateAverage(userExpenses);

        System.out.printf("Statistics for User ID: %s%n", userId);
        System.out.printf("Total: %.2f%n", total);
        System.out.printf("Max: %.2f%n", max);
        System.out.printf("Average: %.2f%n", average);
    }

    static void exportExpensesToConsole() {
        String userId = validateStringInput("Enter User ID: ", "User ID cannot be empty");
        List<Expense> userExpenses = expenseManager.getExpensesByUser(userId);
        if (userExpenses.isEmpty()) {
            System.out.println("No expenses found for user " + userId);
            return;
        }
        exportManager.exportToConsole(userExpenses);
    }


}
