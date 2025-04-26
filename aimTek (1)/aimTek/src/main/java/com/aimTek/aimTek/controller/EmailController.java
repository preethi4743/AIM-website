package com.aimTek.aimTek.controller;

import com.aimTek.aimTek.model.ContactForm;
import com.aimTek.aimTek.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody ContactForm contactForm) {
        try {
            if (contactForm.getFirstName() == null || contactForm.getFirstName().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (contactForm.getLastName() == null || contactForm.getLastName().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (contactForm.getEmail() == null || contactForm.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (contactForm.getPhoneNumber() == null || contactForm.getPhoneNumber().isEmpty()) {
                return ResponseEntity.badRequest().body("Phone number is required");
            }
            if (contactForm.getSubject() == null || contactForm.getSubject().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required");
            }
            if (contactForm.getProjectBudget() == null || contactForm.getProjectBudget().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required");
            } if (contactForm.getProjectDescription() == null || contactForm.getProjectDescription().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required");
            } if (contactForm.getRole() == null || contactForm.getRole().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required");
            }
            if (contactForm.getCompany() == null || contactForm.getCompany().isEmpty()) {
                return ResponseEntity.badRequest().body("Message is required");
            }


            emailService.sendEmail(contactForm);
            return ResponseEntity.ok("Email sent successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<String> subscribe(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");
        String response = emailService.saveEmailSubscription(email);
        if (response.equals("Subscription successful!")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/unsubscribe")
public ResponseEntity<String> unsubscribe(@RequestBody Map<String, String> emailMap){
        String email=emailMap.get("email");
        if(email==null|| email.isEmpty()){
            return ResponseEntity.badRequest().body("Email is required");
        }
        boolean success =emailService.unsubscribeEmail(email);
        if(success){
            return ResponseEntity.ok("successfully unsubscribed");

        }else {
            return ResponseEntity.badRequest().body("Email not found");
        }
    }
}
