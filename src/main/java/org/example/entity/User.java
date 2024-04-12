package org.example.entity;

import lombok.Data;
import org.example.OnlineStoreApp;

@Data
public class User {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void printChoices() {
        System.out.println("View Choices");
    }

    public void performActions(OnlineStoreApp store) {
        System.out.println("Perform Actions");
    }
}
