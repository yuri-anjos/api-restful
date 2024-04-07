package br.com.yuri.studies.restfulspringboot.integrationtests.dtos.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "WrapperPersonDTO")
public class WrapperPersonDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -1798583100149824031L;

	@JsonProperty("_embedded")
	@XmlElement
	private PersonEmbeddedDTO embedded;

	public WrapperPersonDTO() {
		// empty constructor.
	}

	public PersonEmbeddedDTO getEmbedded() {
		return embedded;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WrapperPersonDTO that = (WrapperPersonDTO) o;
		return Objects.equals(embedded, that.embedded);
	}

	@Override
	public int hashCode() {
		return Objects.hash(embedded);
	}
}
