package org.example;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void addUser(String userId, String name) {
        if (userId == null || name == null) {
            throw new IllegalArgumentException("User ID and name cannot be null");
        }

        if (users.containsKey(userId)) {
            return; // Silent return for duplicate IDs
        }

        users.put(userId, new User(userId, name));
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }
}
