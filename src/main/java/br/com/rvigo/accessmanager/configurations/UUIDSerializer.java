package br.com.rvigo.accessmanager.configurations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.UUID;

public class UUIDSerializer extends JsonSerializer<UUID> {
    @Override
    public void serialize(UUID uuid, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(
                "id",
                uuid.toString().replaceAll("-", ""));
        jsonGenerator.writeEndObject();
    }
}
