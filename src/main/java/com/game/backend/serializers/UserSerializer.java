package com.game.backend.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.game.backend.models.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (user != null) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("userId", user.getUserId());
            jsonGenerator.writeStringField("userName", user.getUserName());
            jsonGenerator.writeStringField("staff", String.valueOf(user.isStaff()));
            jsonGenerator.writeEndObject();
        } else {
            jsonGenerator.writeNull();
        }
    }
}
