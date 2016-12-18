package com.absurd.redis.springdata.jackson;

import com.absurd.redis.springdata.dto.UserDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by wangwenwei on 16/12/18.
 */
public class UserSerializer extends JsonSerializer<UserDTO> {
    @Override
    public void serialize(UserDTO userDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", userDTO.getId());
        jsonGenerator.writeStringField("userName",userDTO.getUserName());
        jsonGenerator.writeStringField("password",userDTO.getPassword());
        jsonGenerator.writeEndObject();
    }
}
