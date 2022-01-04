package br.com.rvigo.accessmanager.configurations;

import org.bson.UuidRepresentation;
import org.bson.internal.UuidHelper;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.UUID;

@WritingConverter
public class UUIDToBinaryConverter implements Converter<UUID, Binary> {


    @Override
    public Binary convert(UUID uuid) {
        return new Binary(UuidHelper.encodeUuidToBinary(uuid, UuidRepresentation.PYTHON_LEGACY));
    }
}
