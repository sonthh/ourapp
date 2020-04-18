package com.son.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.son.entity.User;

import java.io.IOException;

public class UserJsonSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", value.getId());
        gen.writeObjectField("username", value.getUsername());
        gen.writeObjectField("fullName", value.getFullName());
        gen.writeObjectField("email", value.getEmail());
        gen.writeObjectField("phoneNumber", value.getPhoneNumber());
        gen.writeObjectField("birthDay", value.getBirthDay());
        gen.writeObjectField("gender", value.getGender());
        gen.writeObjectField("identification", value.getIdentification());
        gen.writeObjectField("address", value.getAddress());
        gen.writeObjectField("avatar", value.getAvatar());
        gen.writeObjectField("status", value.getStatus());
        gen.writeObjectField("shouldSendNotification", value.getShouldSendNotification());
        gen.writeObjectField("notificationTypes", value.getNotificationTypes());
        gen.writeObjectField("roles", value.getRoles());
        gen.writeEndObject();
    }
}