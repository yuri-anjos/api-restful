package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.exceptions.ResourceNotFoundException;
import br.com.yuri.studies.restfulspringboot.exceptions.RuleException;
import br.com.yuri.studies.restfulspringboot.mocks.MockPerson;
import br.com.yuri.studies.restfulspringboot.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	private MockPerson mockPerson;
	private PersonService service;

	@Mock
	private PersonRepository personRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockPerson = new MockPerson();
		service = new PersonService(personRepository);
	}

	@Test
	void findAll() {
		var personList = mockPerson.mockEntityList();
		var personDTOList = mockPerson.mockDTOList();

		when(personRepository.findAll()).thenReturn(personList);

		var result = service.findAll();

		assertNotNull(result);
		assertEquals(10, result.size());

		var p1 = result.get(0);
		var dto1 = personDTOList.get(0);

		assertEquals(dto1.getKey(), p1.getKey());
		assertEquals(dto1.getFirstName(), p1.getFirstName());
		assertEquals(dto1.getLastName(), p1.getLastName());
		assertEquals(dto1.getAddress(), p1.getAddress());
		assertEquals(dto1.getGender(), p1.getGender());
		System.out.println(p1.getLinks());
		assertTrue(p1.getLinks().toString().contains("</person/v1/0>;rel=\"self\""));
	}

	@Test
	void findById() {
		var person = mockPerson.mockEntity();
		var personDTO = mockPerson.mockDTO();

		when(personRepository.findById(1L)).thenReturn(Optional.of(person));

		var result = service.findById(1L);

		assertNotNull(result);
		assertEquals(personDTO.getKey(), result.getKey());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertTrue(result.getLinks().toString().contains("</person/v1/1>;rel=\"self\""));
	}

	@Test
	void findByIdResourceNotFound() {
		when(personRepository.findById(1L)).thenReturn(Optional.empty());

		var exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));

		verify(personRepository).findById(1L);
		assertEquals("Person not found!", exception.getMessage());
	}

	@Test
	void create() {
		var person = mockPerson.mockEntity(1);
		person.setId(null);
		var persisted = mockPerson.mockEntity(1);
		var personDTO = mockPerson.mockDTO(1);
		personDTO.setKey(null);

		when(personRepository.save(person)).thenReturn(persisted);

		var result = service.create(personDTO);

		verify(personRepository).save(person);
		assertNotNull(result);
		assertEquals(1L, result.getKey());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertTrue(result.getLinks().toString().contains("</person/v1/1>;rel=\"self\""));
	}

	@Test
	void update() {
		var person = mockPerson.mockEntity(1);
		var persisted = mockPerson.mockEntity(1);
		var personDTO = mockPerson.mockDTO(1);
		personDTO.setKey(null);

		when(personRepository.findById(1L)).thenReturn(Optional.of(person));
		when(personRepository.save(person)).thenReturn(persisted);
		var result = service.update(1L, personDTO);

		verify(personRepository).findById(1L);
		verify(personRepository).save(person);

		assertNotNull(result);
		assertEquals(1L, result.getKey());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertTrue(result.getLinks().toString().contains("</person/v1/1>;rel=\"self\""));
	}

	@Test
	void updateResourceNotFoundException() {
		var personDTO = mockPerson.mockDTO(1);

		when(personRepository.findById(1L)).thenReturn(Optional.empty());
		var exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(1L, personDTO));

		verify(personRepository).findById(1L);
		verifyNoMoreInteractions(personRepository);
		assertEquals("Person not found!", exception.getMessage());
	}

	@Test
	void deleteByIdSuccess() {
		var person = mockPerson.mockEntity(1);

		when(personRepository.findById(1L)).thenReturn(Optional.of(person));

		service.deleteById(1L);

		verify(personRepository).findById(1L);
		verify(personRepository).delete(person);
	}

	@Test
	void deleteByIdResourceNotFound() {
		when(personRepository.findById(1L)).thenReturn(Optional.empty());

		var exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));

		verify(personRepository).findById(1L);
		verifyNoMoreInteractions(personRepository);
		assertEquals("Person not found!", exception.getMessage());
	}

	@Test
	void disablePersonSuccess() {
		var entity = mockPerson.mockEntity(1);

		when(personRepository.findById(1L)).thenReturn(Optional.of(entity));

		service.disablePerson(1L);

		verify(personRepository).findById(1L);
		verify(personRepository).disablePerson(1L);
	}

	@Test
	void disablePersonAlreadyDisabled() {
		var entity = mockPerson.mockEntity(1);
		entity.setEnabled(Boolean.FALSE);

		when(personRepository.findById(1L)).thenReturn(Optional.of(entity));

		var exception = assertThrows(RuleException.class, () -> service.disablePerson(1L));

		verify(personRepository).findById(1L);
		verifyNoMoreInteractions(personRepository);
		assertEquals("Person is already disabled!", exception.getMessage());
	}

	@Test
	void disablePersonNotFound() {
		when(personRepository.findById(1L)).thenReturn(Optional.empty());

		var exception = assertThrows(ResourceNotFoundException.class, () -> service.disablePerson(1L));

		verify(personRepository).findById(1L);
		verifyNoMoreInteractions(personRepository);
		assertEquals("Person not found!", exception.getMessage());
	}
}
