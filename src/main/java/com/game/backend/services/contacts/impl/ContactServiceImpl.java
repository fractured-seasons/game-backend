package com.game.backend.services.contacts.impl;

import com.game.backend.models.Contact;
import com.game.backend.repositories.ContactRepository;
import com.game.backend.services.contacts.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Override
    public void processContactMessage(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    @Transactional
    public Page<Contact> getAllContacts(Pageable pageable) {
        return contactRepository.findAllByOrderByCreatedAtAsc(pageable);
    }

    @Override
    public Contact getContact(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
    }
}
