package br.com.yuri.studies.restfulspringboot.integrationtests.controllers.yaml.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YamlMapper implements ObjectMapper {

	private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
	protected TypeFactory typeFactory;

	public YamlMapper() {
		objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		typeFactory = TypeFactory.defaultInstance();
	}

	@Override
	public Object deserialize(ObjectMapperDeserializationContext context) {
		try {
			return objectMapper.readValue(
					context.getDataToDeserialize().asString(),
					objectMapper.constructType(context.getType())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object serialize(ObjectMapperSerializationContext context) {
		try {
			return objectMapper.writeValueAsString(
					context.getObjectToSerialize()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}