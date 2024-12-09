package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testValidUserCreation() {
        User user = new User("123", "Alice");
        assertNotNull(user);
        assertEquals("123", user.getId());
        assertEquals("Alice", user.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("123", "Alice");
        User user2 = new User("123", "Alice");
        User user3 = new User("456", "Alice");

        // Test equals
        assertTrue(user1.equals(user1));      // Same object
        assertTrue(user1.equals(user2));      // Same values
        assertTrue(user2.equals(user1));      // Symmetry
        assertFalse(user1.equals(user3));     // Different values
        assertFalse(user1.equals(null));      // Null check
        assertFalse(user1.equals(new Object())); // Different type

        // Test hashCode
        assertEquals(user1.hashCode(), user2.hashCode());     // Same values = same hash
        assertNotEquals(user1.hashCode(), user3.hashCode());  // Different values = different hash
    }
    @Test
    void testTrimming() {
        User user = new User("123", "Alice");
        assertEquals("123", user.getId());
        assertEquals("Alice", user.getName());
    }

    @ParameterizedTest
    @CsvSource({
            "   , Bob, User ID cannot be null or empty",
            "456,    , Name cannot be null or empty"
    })
    void testInvalidUserCreation_WhitespaceInputs(String id, String name, String expectedErrorMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(id, name)
        );
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void testInvalidUserCreation_NullInputs() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "Alice"));
        assertThrows(IllegalArgumentException.class, () -> new User("123", null));
    }

    @ParameterizedTest
    @MethodSource("provideNullInputs")
    void testInvalidUserCreation_NullInputs(String id, String name, String expectedErrorMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new User(id, name)
        );
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    private static Stream<Arguments> provideNullInputs() {
        return Stream.of(
                Arguments.of(null, "Alice", "User ID cannot be null or empty"),
                Arguments.of("123", null, "Name cannot be null or empty"),
                Arguments.of(null, null, "User ID cannot be null or empty")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "abc123, John Doe",
            "456xyz, Jane Smith",
            "789pqr, Mark O'Connor"
    })
    void testValidUserCreationWithDifferentValues(String id, String name) {
        User user = new User(id, name);
        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
    }
}
