package org.example;

import org.example.entity.User;

public class Menu2 {
    public static void main(String[] args) {
        OnlineStoreApp store = new OnlineStoreApp();
        User user;
        do {
            user = store.landingPage();
            user.performActions(store);
        } while (user != null);
    }
}
