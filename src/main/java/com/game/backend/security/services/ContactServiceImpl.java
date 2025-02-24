package com.game.backend.security.services;

import com.game.backend.models.Contact;
import com.game.backend.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;
    @Override
    public void processContactMessage(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public Page<Contact> getAllContacts(Pageable pageable) {
        return contactRepository.findAllByOrderByCreatedAtAsc(pageable);
    }
}
