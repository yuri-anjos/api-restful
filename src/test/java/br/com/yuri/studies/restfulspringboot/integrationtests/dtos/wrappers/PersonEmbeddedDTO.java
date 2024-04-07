package br.com.yuri.studies.restfulspringboot.integrationtests.dtos.wrappers;

import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@XmlRootElement(name = "PersonEmbeddedDTO")
public class PersonEmbeddedDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -119495945319253529L;

	@JsonProperty("personDTOList")
	@XmlElement
	private List<PersonDTO> persons;

	public PersonEmbeddedDTO() {
		// empty constructor.
	}

	public List<PersonDTO> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonDTO> persons) {
		this.persons = persons;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PersonEmbeddedDTO that = (PersonEmbeddedDTO) o;
		return Objects.equals(persons, that.persons);
	}

	@Override
	public int hashCode() {
		return Objects.hash(persons);
	}
}
