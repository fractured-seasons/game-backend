package com.game.backend.repositories.wiki;

import com.game.backend.models.User;
import com.game.backend.models.wiki.WikiContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WikiContributionRepository extends JpaRepository<WikiContribution, Long> {
    @Query("SELECT DISTINCT wc.user FROM WikiContribution wc")
    List<User> findDistinctContributors();
    @Query("SELECT DISTINCT wc.user FROM WikiContribution wc WHERE wc.wikiArticle.slug = :slug")
    List<User> findContributorsByArticleSlug(@Param("slug") String slug);
}
