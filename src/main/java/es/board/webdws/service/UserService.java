package es.board.webdws.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private Map<String, String> users = new HashMap<>();

    public UserService() {
        users.put("admin", "admin");
    }

    public boolean validateUser(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}
