package it.arico.manage_users.model;

public class UserCreatedEventDTO {

    private final Long userId;
    private final String username;

    public UserCreatedEventDTO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
