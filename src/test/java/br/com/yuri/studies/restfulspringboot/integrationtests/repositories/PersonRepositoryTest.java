package br.com.yuri.studies.restfulspringboot.integrationtests.repositories;

import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.yuri.studies.restfulspringboot.models.Person;
import br.com.yuri.studies.restfulspringboot.repositories.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	public PersonRepository repository;

	private static Person person;

	@BeforeAll
	static void beforeAll() {
		person = new Person();
	}

	@Test
	@Order(1)
	void testDisablePerson() {
		var result = repository.disablePerson(1L);

		person = repository.findById(1L).orElse(null);

		Assertions.assertEquals(1, result);
		Assertions.assertFalse(person.getEnabled());
	}
}
