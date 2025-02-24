package com.game.backend.repositories;

import com.game.backend.models.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findAllByOrderByCreatedAtAsc(Pageable pageable);
}
