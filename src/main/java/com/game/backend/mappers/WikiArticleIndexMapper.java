package com.game.backend.mappers;

import com.game.backend.dtos.wiki.ArticleIndexDTO;
import com.game.backend.models.wiki.WikiArticle;
import org.springframework.stereotype.Component;

@Component
public class WikiArticleIndexMapper {
    public ArticleIndexDTO toIndexDTO(WikiArticle article) {
        ArticleIndexDTO dto = new ArticleIndexDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setSlug(article.getSlug());

        if (article.getCategory() != null) {
            dto.setCategoryId(article.getCategory().getId());
            dto.setCategoryTitle(article.getCategory().getTitle());
        }

        if (article.getCreatedBy() != null) {
            dto.setCreatedByUsername(article.getCreatedBy().getUserName());
        }

        return dto;
    }
}
