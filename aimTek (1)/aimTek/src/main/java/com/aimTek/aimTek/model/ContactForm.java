package com.aimTek.aimTek.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "contact_forms")
public class ContactForm {

    @Id
    private String id; // MongoDB ID

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String subject;
    private String projectBudget;
    private String projectDescription;
    private String role;
    private String company;

}
