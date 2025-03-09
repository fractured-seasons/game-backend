package com.game.backend.services.wiki.impl;

import com.game.backend.dtos.wiki.ArticleDTO;
import com.game.backend.dtos.wiki.ArticleResponseDTO;
import com.game.backend.models.User;
import com.game.backend.models.wiki.ApprovalStatus;
import com.game.backend.models.wiki.WikiArticle;
import com.game.backend.models.wiki.WikiCategory;
import com.game.backend.models.wiki.WikiContribution;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.wiki.WikiArticleRepository;
import com.game.backend.repositories.wiki.WikiCategoryRepository;
import com.game.backend.repositories.wiki.WikiContributionRepository;
import com.game.backend.services.wiki.WikiArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WikiArticleServiceImpl implements WikiArticleService {
    private final WikiArticleRepository wikiArticleRepository;
    private final UserRepository userRepository;
    private final WikiCategoryRepository wikiCategoryRepository;
    private final WikiContributionRepository wikiContributionRepository;

    public WikiArticleServiceImpl(final WikiArticleRepository wikiArticleRepository,
                                  final UserRepository userRepository,
                                  final WikiCategoryRepository wikiCategoryRepository,
                                  final WikiContributionRepository wikiContributionRepository) {
        this.wikiArticleRepository = wikiArticleRepository;
        this.userRepository = userRepository;
        this.wikiCategoryRepository = wikiCategoryRepository;
        this.wikiContributionRepository = wikiContributionRepository;
    }

    @Override
    public WikiArticle createWikiArticle(ArticleDTO article, UserDetails userDetails) {
        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        WikiCategory wikiCategory = wikiCategoryRepository.findById(article.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Wiki category not found"));

        if (wikiArticleRepository.existsByTitle(article.getTitle())) {
            throw new RuntimeException("Title must be unique");
        }

        WikiArticle newArticle = new WikiArticle();
        newArticle.setCategory(wikiCategory);
        newArticle.setCreatedBy(currentUser);
        newArticle.setTitle(article.getTitle());
        newArticle.setContent(article.getContent());
        newArticle.setApprovalStatus(ApprovalStatus.PENDING);
        return wikiArticleRepository.save(newArticle);
    }

    @Override
    public List<WikiArticle> getApprovedArticles() {
        return wikiArticleRepository.findByApprovalStatusAndHiddenFalse(ApprovalStatus.APPROVED);
    }

    @Override
    public List<WikiArticle> getPendingArticles() {
        return wikiArticleRepository.findByApprovalStatusAndHiddenFalse(ApprovalStatus.PENDING);
    }

    @Override
    public Page<ArticleResponseDTO> getAllArticles(Pageable pageable) {
        return wikiArticleRepository.findAllByCustomApprovalOrder(pageable);
    }

    @Override
    public void approveArticle(Long articleId, UserDetails userDetails) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User contributorUser = article.getCreatedBy();
        if (contributorUser.getWikiContributions() == null) {
            contributorUser.setWikiContributions(0);
        } else {
            contributorUser.setWikiContributions(contributorUser.getWikiContributions() + 1);
        }

        WikiContribution newWikiContribution = new WikiContribution();
        newWikiContribution.setUser(article.getCreatedBy());
        newWikiContribution.setWikiArticle(article);
        wikiContributionRepository.save(newWikiContribution);

        article.setApprovalStatus(ApprovalStatus.APPROVED);
        article.setApprovedBy(currentUser);
        wikiArticleRepository.save(article);
    }

    @Override
    public void rejectArticle(Long articleId, UserDetails userDetails) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Wiki article not found"));

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        article.setApprovalStatus(ApprovalStatus.REJECTED);
        article.setRejectedBy(currentUser);
        wikiArticleRepository.save(article);
    }

    @Override
    public WikiArticle updateWikiArticle(Long articleId, @Valid ArticleDTO updatedArticle, UserDetails userDetails) {
        WikiArticle wikiArticle = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Wiki article not found"));

        WikiCategory wikiCategory = wikiCategoryRepository.findById(updatedArticle.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Wiki category not found"));

        if (!Objects.equals(wikiArticle.getTitle(), updatedArticle.getTitle()) && wikiArticleRepository.existsByTitle(updatedArticle.getTitle())) {
            throw new RuntimeException("Title must be unique");
        }

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WikiContribution newWikiContribution = new WikiContribution();
        newWikiContribution.setUser(currentUser);
        newWikiContribution.setWikiArticle(wikiArticle);
        wikiContributionRepository.save(newWikiContribution);

        wikiArticle.setTitle(updatedArticle.getTitle());
        wikiArticle.setContent(updatedArticle.getContent());
        wikiArticle.setCategory(wikiCategory);
        wikiArticle.setApprovalStatus(ApprovalStatus.PENDING);

        return wikiArticleRepository.save(wikiArticle);
    }

    @Override
    public void deleteWikiArticleById(Long articleId) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        wikiArticleRepository.delete(article);
    }

    @Override
    public WikiArticle getWikiArticleBySlug(String articleSlug) {
        return wikiArticleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new RuntimeException("Wiki article not found"));
    }

    @Override
    public void toggleArticleVisibility(Long articleId, UserDetails userDetails) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Wiki article not found"));

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        article.setHidden(!article.isHidden());
        if (article.isHidden()) {
            article.setHiddenBy(currentUser);
        }

        wikiArticleRepository.save(article);
    }
}
