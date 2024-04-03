package br.com.yuri.studies.restfulspringboot.mocks;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.models.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

	public Person mockEntity() {
		return mockEntity(0);
	}

	public PersonDTO mockDTO() {
		return mockDTO(0);
	}

	public List<Person> mockEntityList() {
		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			persons.add(mockEntity(i));
		}
		return persons;
	}

	public List<PersonDTO> mockDTOList() {
		List<PersonDTO> persons = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			persons.add(mockDTO(i));
		}
		return persons;
	}

	public Person mockEntity(Integer number) {
		var person = new Person();
		person.setAddress("Addres Test" + number);
		person.setFirstName("First Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setId(number.longValue());
		person.setLastName("Last Name Test" + number);
		person.setEnabled(Boolean.TRUE);
		return person;
	}

	public PersonDTO mockDTO(Integer number) {
		var person = new PersonDTO();
		person.setAddress("Addres Test" + number);
		person.setFirstName("First Name Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setKey(number.longValue());
		person.setLastName("Last Name Test" + number);
		person.setEnabled(Boolean.TRUE);
		return person;
	}
}