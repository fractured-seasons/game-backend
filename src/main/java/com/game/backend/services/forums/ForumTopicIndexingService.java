package com.game.backend.services.forums;

import com.game.backend.models.forums.ForumTopic;

public interface ForumTopicIndexingService {
    void indexForumTopic(ForumTopic topic);
    void removeIndexedTopic(Long topicId);
}
