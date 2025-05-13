package com.game.backend.dtos.forum;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "forum_topic")
public class TopicIndexDTO {
    private Long id;
    
    @Field(type = FieldType.Text)
    private String title;
    
    @Field(type = FieldType.Text)
    private String content;
    
    @Field(type = FieldType.Long)
    private Long categoryId;
    
    @Field(type = FieldType.Text)
    private String categoryName;
    
    @Field(type = FieldType.Boolean)
    private boolean locked;
    
    @Field(type = FieldType.Boolean)
    private boolean pinned;
    
    @Field(type = FieldType.Boolean)
    private boolean hidden;
    
    @Field(type = FieldType.Text)
    private String createdByUsername;
}