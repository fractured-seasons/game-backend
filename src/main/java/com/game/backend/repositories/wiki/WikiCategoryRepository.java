package com.game.backend.repositories.wiki;

import com.game.backend.models.wiki.WikiCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
    @Query("SELECT DISTINCT c FROM WikiCategory c JOIN FETCH c.articles a WHERE a.approvalStatus = 'APPROVED' AND a.hidden = false")
    List<WikiCategory> findAllWithApprovedArticles();
}
