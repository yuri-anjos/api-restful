package br.com.yuri.studies.restfulspringboot.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class YamlHttpConverter extends AbstractJackson2HttpMessageConverter {
	protected YamlHttpConverter() {
		super(new YAMLMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL), MediaType.parseMediaType("application/x-yaml"));
	}
}
