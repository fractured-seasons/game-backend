package com.game.backend.controllers;

import com.game.backend.models.Contact;
import com.game.backend.security.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<String> handleContactForm(@RequestBody Contact contact) {
        try {
            contactService.processContactMessage(contact);
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send the message.");
        }
    }
}
