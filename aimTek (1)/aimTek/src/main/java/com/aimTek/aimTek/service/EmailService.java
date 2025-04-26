package com.aimTek.aimTek.service;

import com.aimTek.aimTek.Repository.ContactFormRepository;
import com.aimTek.aimTek.Repository.EmailRepository;
import com.aimTek.aimTek.model.ContactForm;
import com.aimTek.aimTek.model.EmailSubscription;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ContactFormRepository contactFormRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(ContactForm contactForm) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            if (!isValidEmail(contactForm.getEmail())) {
                throw new MessagingException("Invalid email address: " + contactForm.getEmail());
            }

            helper.setTo("kpr65365@gmail.com"); // Admin Email
            helper.setFrom(contactForm.getEmail());
            helper.setSubject("New Contact Form Submission: " + contactForm.getSubject());

            StringBuilder body = new StringBuilder();
            body.append("<p><strong>First Name:</strong> ").append(contactForm.getFirstName()).append("</p>");
            body.append("<p><strong>Last Name:</strong> ").append(contactForm.getLastName()).append("</p>");
            body.append("<p><strong>Email:</strong> ").append(contactForm.getEmail()).append("</p>");
            body.append("<p><strong>Phone Number:</strong> ").append(contactForm.getPhoneNumber()).append("</p>");
            body.append("<p><strong>Subject:</strong> ").append(contactForm.getSubject()).append("</p>");
            body.append("<p><strong>Role:</strong> ").append(contactForm.getRole()).append("</p>");
            body.append("<p><strong>Project Description:</strong> ").append(contactForm.getProjectDescription()).append("</p>");
            body.append("<p><strong>Company:</strong> ").append(contactForm.getCompany()).append("</p>");
            body.append("<p><strong>Project Budget:</strong> ").append(contactForm.getProjectBudget()).append("</p>");

            helper.setText(body.toString(), true); // HTML content
            javaMailSender.send(mimeMessage);

            sendReplyEmail(contactForm);
            saveContactForm(contactForm);
            saveEmailSubscription(contactForm.getEmail());

        } catch (MessagingException e) {
            logger.error("Error processing contact form and sending email: {}", e.getMessage(), e);
            throw new MessagingException("Error processing contact form and attachments.", e);
        }
    }

    private void saveContactForm(ContactForm contactForm) {
        contactFormRepository.save(contactForm);
    }

    public String saveEmailSubscription(String email) {
        if (!isValidEmail(email)) {
            return "Invalid email address.";
        }
        if (!emailRepository.existsByEmail(email)) {
            EmailSubscription emailSubscription = new EmailSubscription(email);
            emailRepository.save(emailSubscription);
            try {
                sendSubscriptionEmail(email);
                return "Subscription successful!";
            } catch (MessagingException e) {
                logger.error("Error sending subscription confirmation email: {}", e.getMessage(), e);
                return "Error sending subscription confirmation email.";
            }
        } else {
            return "Email is already subscribed.";
        }
    }

    private void sendReplyEmail(ContactForm contactForm) throws MessagingException {
        MimeMessage replyMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper replyHelper = new MimeMessageHelper(replyMessage, true);

        if (!isValidEmail(contactForm.getEmail())) {
            throw new MessagingException("Invalid email address: " + contactForm.getEmail());
        }

        replyHelper.setTo(contactForm.getEmail());
        replyHelper.setFrom("no-reply@example.com");
        replyHelper.setSubject("Thank you for contacting us");
        replyHelper.setText("Thank you for your submission! We will get back to you shortly.");
        javaMailSender.send(replyMessage);
    }

    private void sendSubscriptionEmail(String email) throws MessagingException {
        MimeMessage subscriptionMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper subscriptionHelper = new MimeMessageHelper(subscriptionMessage, true);

        if (!isValidEmail(email)) {
            throw new MessagingException("Invalid email address: " + email);
        }

        subscriptionHelper.setTo(email);
        subscriptionHelper.setFrom("no-reply@example.com");
        subscriptionHelper.setSubject("Subscription Confirmation");
        subscriptionHelper.setText("You have successfully subscribed to our mailing list.");
        javaMailSender.send(subscriptionMessage);
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    public boolean unsubscribeEmail(String email){
        if (!isValidEmail(email)) {
            return false;
        }
        Optional<EmailSubscription> subscription =emailRepository.findByEmail(email);
        if(subscription.isPresent()){
            emailRepository.delete(subscription.get());
            try{
                sendUnsubscribeConfirmationEmail(email);
                return true;

            }catch (MessagingException e){
                logger.error("Failed to send Unsubscribe confirmation email:{}",e.getMessage());
                return false;
            }
        }
        return false;
    }

    public void sendUnsubscribeConfirmationEmail(String email) throws MessagingException {
        MimeMessage unsubscribeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(unsubscribeMessage,true);
        helper.setTo(email);
        helper.setFrom("no-reply@example.com");
        helper.setSubject("You have been unsubscribed");
        helper.setText("You have successfully unsubscribed from our mailing list.");
        javaMailSender.send(unsubscribeMessage);
    }
}
