package com.game.backend.repositories.forums;

import com.game.backend.models.forums.ForumSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumSectionRepository extends JpaRepository<ForumSection, Long> {
    Optional<ForumSection> findByName(String sectionName);
}
