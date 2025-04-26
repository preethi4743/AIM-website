package com.aimTek.aimTek.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_subscriptions")
public class EmailSubscription {

    @Id
    private String id; // Use String for the ID in MongoDB

    private String email;

    public EmailSubscription() {}

    public EmailSubscription(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
