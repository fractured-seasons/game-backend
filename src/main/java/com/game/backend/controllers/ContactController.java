package com.game.backend.controllers;

import com.game.backend.models.Contact;
import com.game.backend.security.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<String> handleContactForm(@RequestBody @Valid Contact contact) {
        contactService.processContactMessage(contact);
        return ResponseEntity.ok("Message sent successfully!");
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_SUPPORT')")
    public ResponseEntity<Page<Contact>> getAllContacts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Contact> contactsPage = contactService.getAllContacts(pageable);

        return new ResponseEntity<>(contactsPage, HttpStatus.OK);
    }
}
