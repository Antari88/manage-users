package it.arico.manage_users.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String name) {
        super("Role not found with name: " + name);
    }
}
