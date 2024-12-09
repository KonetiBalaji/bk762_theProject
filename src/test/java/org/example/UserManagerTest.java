package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void testAddUser() {
        userManager.addUser("123", "Alice");
        assertTrue(userManager.userExists("123"));
        User user = userManager.getUser("123");
        assertNotNull(user);
        assertEquals("Alice", user.getName());
    }

    @Test
    void testAddDuplicateUser() {
        userManager.addUser("123", "Alice");
        userManager.addUser("123", "Bob");
        User user = userManager.getUser("123");
        assertEquals("Alice", user.getName()); // Original user should remain
    }

    @Test
    void testGetNonExistentUser() {
        assertNull(userManager.getUser("nonexistent"));
    }

    @ParameterizedTest
    @CsvSource({
            "123, Alice",
            "456, Bob",
            "789, Charlie"
    })
    void testMultipleUsers(String id, String name) {
        userManager.addUser(id, name);
        assertTrue(userManager.userExists(id));
        assertEquals(name, userManager.getUser(id).getName());
    }

    @Test
    void testUserExists() {
        assertFalse(userManager.userExists("123"));
        userManager.addUser("123", "Alice");
        assertTrue(userManager.userExists("123"));
    }

    @Test
    void testAddUserWithInvalidInputs() {
        assertThrows(IllegalArgumentException.class,
                () -> userManager.addUser(null, "Alice"));
        assertThrows(IllegalArgumentException.class,
                () -> userManager.addUser("123", null));
        assertThrows(IllegalArgumentException.class,
                () -> userManager.addUser("", "Alice"));
        assertThrows(IllegalArgumentException.class,
                () -> userManager.addUser("123", ""));
    }
}

