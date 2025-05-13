package com.game.backend.search;

import com.game.backend.dtos.wiki.ArticleIndexDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WikiArticleSearchRepository extends ElasticsearchRepository<ArticleIndexDTO, Long> {
    List<ArticleIndexDTO> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
}
