package com.game.backend.security.services;

import com.game.backend.models.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    void processContactMessage(Contact contact);

    Page<Contact> getAllContacts(Pageable pageable);
}
