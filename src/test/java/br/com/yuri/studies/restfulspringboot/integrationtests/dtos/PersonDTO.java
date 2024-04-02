package br.com.yuri.studies.restfulspringboot.integrationtests.dtos;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement(name = "PersonDTO")
public class PersonDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -5993411721511493424L;

	private Long id;
	private String firstName;
	private String lastName;
	private String address;
	private String gender;

	public PersonDTO() {
		// empty constructor.
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PersonDTO personDTO = (PersonDTO) o;
		return Objects.equals(id, personDTO.id)
				&& Objects.equals(firstName, personDTO.firstName)
				&& Objects.equals(lastName, personDTO.lastName)
				&& Objects.equals(address, personDTO.address)
				&& Objects.equals(gender, personDTO.gender);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, gender);
	}
}
