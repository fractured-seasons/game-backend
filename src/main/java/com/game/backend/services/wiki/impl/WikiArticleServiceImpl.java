package com.game.backend.services.wiki.impl;

import com.game.backend.models.User;
import com.game.backend.models.wiki.ApprovalStatus;
import com.game.backend.models.wiki.WikiArticle;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.wiki.WikiArticleRepository;
import com.game.backend.services.wiki.WikiArticleService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiArticleServiceImpl implements WikiArticleService {
    private final WikiArticleRepository wikiArticleRepository;
    private final UserRepository userRepository;

    public WikiArticleServiceImpl(WikiArticleRepository wikiArticleRepository, UserRepository userRepository) {
        this.wikiArticleRepository = wikiArticleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WikiArticle createWikiArticle(WikiArticle article) {
        return null;
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
    public List<WikiArticle> getAllArticles() {
        return wikiArticleRepository.findAll();
    }

    @Override
    public void approveArticle(Long articleId, UserDetails userDetails) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        article.setApprovalStatus(ApprovalStatus.APPROVED);
        article.setApprovedBy(currentUser);
        wikiArticleRepository.save(article);
    }

    @Override
    public void rejectArticle(Long articleId, UserDetails userDetails) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        article.setApprovalStatus(ApprovalStatus.REJECTED);
        article.setRejectedBy(currentUser);
        wikiArticleRepository.save(article);
    }

    @Override
    public WikiArticle updateWikiArticle(Long articleId, WikiArticle updatedCategory) {
        return null;
    }

    @Override
    public void deleteWikiArticle(Long articleId) {
        WikiArticle article = wikiArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        wikiArticleRepository.delete(article);
    }
}
