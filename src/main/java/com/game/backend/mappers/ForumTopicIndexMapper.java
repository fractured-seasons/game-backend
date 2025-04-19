package com.game.backend.mappers;

import com.game.backend.models.forums.ForumTopic;
import com.game.backend.dtos.forum.TopicIndexDTO;
import org.springframework.stereotype.Component;

@Component
public class ForumTopicIndexMapper {
    public TopicIndexDTO toIndexDTO(ForumTopic topic) {
        TopicIndexDTO dto = new TopicIndexDTO();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setLocked(topic.isLocked());
        dto.setPinned(topic.isPinned());
        dto.setHidden(topic.isHidden());
        
        if (topic.getCategory() != null) {
            dto.setCategoryId(topic.getCategory().getId());
            dto.setCategoryName(topic.getCategory().getName());
        }
        
        if (topic.getCreatedBy() != null) {
            dto.setCreatedByUsername(topic.getCreatedBy().getUserName());
        }
        
        return dto;
    }
}