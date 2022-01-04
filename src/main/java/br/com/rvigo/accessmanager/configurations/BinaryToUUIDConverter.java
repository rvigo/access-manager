package br.com.rvigo.accessmanager.configurations;

import org.bson.UuidRepresentation;
import org.bson.internal.UuidHelper;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class BinaryToUUIDConverter implements Converter<Binary, UUID> {

    @Override
    public UUID convert(Binary binary) {
        Integer type = 4;
        return UuidHelper.decodeBinaryToUuid(binary.getData().clone(), type.byteValue(), UuidRepresentation.PYTHON_LEGACY);
    }
}
