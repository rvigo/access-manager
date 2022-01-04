package br.com.rvigo.accessmanager.configurations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class UUIDDeserializer extends JsonDeserializer<UUID> {
    @Override
    public UUID deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCodec().<JsonNode>readTree(jsonParser).has("_id")) {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String id = node.get("_id").asText();
            log.info("got id: " + id);
            return UUID.fromString(id.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));

        } else {
            throw new RuntimeException("cannot convert type");
        }
    }
}
