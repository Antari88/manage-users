package it.arico.manage_users.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.arico.manage_users.model.UserCreatedEventDTO;

@Component
public class UserCreatedListener {

    @Async
    @EventListener
    public void onUserCreated(UserCreatedEventDTO event) {

        System.out.println(
            "[ASYNC EVENT] Utente creato -> id="
            + event.getUserId()
            + " username="
            + event.getUsername()
        );
    }
}
