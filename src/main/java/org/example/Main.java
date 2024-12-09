package org.example;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);  // Changed to non-final for testing

    public static void main(String[] args) {
        runMenu();
    }

    static void runMenu() {  // Made package-private for testing
        boolean continueRunning = true;

        while (continueRunning) {
            printMenu();
            String input = getInput();

            if (input == null || input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            try {
                int choice = parseChoice(input);
                continueRunning = handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        closeScanner();
    }

    static String getInput() {  // Added for testing
        return scanner.nextLine().trim();
    }

    static int parseChoice(String input) throws NumberFormatException {  // Added for testing
        return Integer.parseInt(input);
    }

    static void closeScanner() {  // Added for testing
        if (scanner != null) {
            scanner.close();
        }
    }

    static void setScanner(Scanner newScanner) {  // Added for testing
        scanner = newScanner;
    }

    static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Option 1");
        System.out.println("2. Option 2");
        System.out.println("3. Option 3");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");
    }

    static boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.println("You chose Option 1.");
                break;
            case 2:
                System.out.println("You chose Option 2.");
                break;
            case 3:
                System.out.println("You chose Option 3.");
                break;
            case 4:
                System.out.println("Exiting program. Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
        return true;
    }
}