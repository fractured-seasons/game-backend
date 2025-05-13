package com.game.backend.dtos.wiki;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "wiki_article")
public class ArticleIndexDTO {
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Text)
    private String categoryTitle;

    @Field(type = FieldType.Text)
    private String slug;

    @Field(type = FieldType.Text)
    private String createdByUsername;
}
