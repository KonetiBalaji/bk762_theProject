package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;  // Changed from ByteArrayInputStream to InputStream

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        Main.closeScanner();
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        Main.setScanner(new Scanner(testIn));
    }


    @Test
    void testValidMenuOptions() {
        provideInput("1\n2\n3\n4\n");
        Main.runMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Option 1"));
        assertTrue(output.contains("Option 2"));
        assertTrue(output.contains("Option 3"));
        assertTrue(output.contains("Exiting program"));
    }

    @Test
    void testEmptyInput() {
        provideInput("\n4\n");
        Main.runMenu();
        assertTrue(outContent.toString().contains("Input cannot be empty"));
    }

    @Test
    void testWhitespaceInput() {
        provideInput("   \n4\n");
        Main.runMenu();
        assertTrue(outContent.toString().contains("Input cannot be empty"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abc", "1.5", "-1", "999"})
    void testInvalidInputs(String invalidInput) {
        provideInput(invalidInput + "\n4\n");
        Main.runMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Invalid") || output.contains("valid option"));
    }

    @Test
    void testParseChoice() {
        assertThrows(NumberFormatException.class, () -> Main.parseChoice("abc"));
        assertThrows(NumberFormatException.class, () -> Main.parseChoice("1.5"));
        assertEquals(1, Main.parseChoice("1"));
    }

    @Test
    void testHandleMenuChoice() {
        assertTrue(Main.handleMenuChoice(1));
        assertTrue(Main.handleMenuChoice(2));
        assertTrue(Main.handleMenuChoice(3));
        assertFalse(Main.handleMenuChoice(4));
        assertTrue(Main.handleMenuChoice(5));  // Invalid option
    }

    @Test
    void testMultipleInvalidInputsBeforeValid() {
        provideInput("abc\n123\n-1\n4\n");
        Main.runMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Invalid") || output.contains("valid option"));
        assertTrue(output.contains("Exiting program"));
    }

    @Test
    void testPrintMenu() {
        Main.printMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Menu:"));
        assertTrue(output.contains("1. Option 1"));
        assertTrue(output.contains("2. Option 2"));
        assertTrue(output.contains("3. Option 3"));
        assertTrue(output.contains("4. Exit"));
    }

    @Test
    void testScannerClosed() {
        Scanner testScanner = new Scanner(System.in);
        Main.setScanner(testScanner);
        Main.closeScanner();
        assertThrows(IllegalStateException.class, testScanner::nextLine);
    }
}
