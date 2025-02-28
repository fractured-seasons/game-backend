package com.game.backend.repositories.wiki;

import com.game.backend.models.wiki.ApprovalStatus;
import com.game.backend.models.wiki.WikiArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WikiArticleRepository extends JpaRepository<WikiArticle, Long> {
    List<WikiArticle> findByApprovalStatusAndHiddenFalse(ApprovalStatus approvalStatus);
}
