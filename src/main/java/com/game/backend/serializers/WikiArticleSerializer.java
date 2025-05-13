package com.game.backend.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.game.backend.models.wiki.WikiArticle;

import java.io.IOException;

public class WikiArticleSerializer extends JsonSerializer<WikiArticle> {
    @Override
    public void serialize(WikiArticle wikiArticle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (wikiArticle != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("articleId", wikiArticle.getId());
            jsonGenerator.writeStringField("slug", wikiArticle.getSlug());
            jsonGenerator.writeStringField("title", wikiArticle.getTitle());
            jsonGenerator.writeEndObject();
        } else {
            jsonGenerator.writeNull();
        }
    }
}
