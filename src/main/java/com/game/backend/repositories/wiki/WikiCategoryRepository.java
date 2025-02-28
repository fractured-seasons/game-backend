package com.game.backend.repositories.wiki;

import com.game.backend.models.wiki.WikiCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
}
