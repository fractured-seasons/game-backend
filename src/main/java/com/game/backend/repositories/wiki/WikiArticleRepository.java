package com.game.backend.repositories.wiki;

import com.game.backend.dtos.wiki.ArticleResponseDTO;
import com.game.backend.models.wiki.ApprovalStatus;
import com.game.backend.models.wiki.WikiArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WikiArticleRepository extends JpaRepository<WikiArticle, Long> {
    List<WikiArticle> findByApprovalStatusAndHiddenFalse(ApprovalStatus approvalStatus);

    Optional<WikiArticle> findBySlug(String articleSlug);

    boolean existsByTitle(String title);

    @Query("""
    SELECT new com.game.backend.dtos.wiki.ArticleResponseDTO(
        w.id, w.title, w.slug, w.approvalStatus, w.hidden, w.createdAt, w.category.title
    )
    FROM WikiArticle w
    ORDER BY 
        CASE 
            WHEN w.approvalStatus = 'PENDING' THEN 1 
            WHEN w.approvalStatus = 'APPROVED' THEN 2 
            WHEN w.approvalStatus = 'REJECTED' THEN 3 
        END
""")
    Page<ArticleResponseDTO> findAllByCustomApprovalOrder(Pageable pageable);

}
