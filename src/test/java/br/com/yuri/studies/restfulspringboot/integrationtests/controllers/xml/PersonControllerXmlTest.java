package br.com.yuri.studies.restfulspringboot.integrationtests.controllers.xml;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.TokenDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static PersonDTO personDTO;

	@BeforeAll
	public static void setUp() {
		personDTO = new PersonDTO();
	}

	private void mockPerson() {
		personDTO.setFirstName("Adam");
		personDTO.setLastName("Sandler");
		personDTO.setAddress("New York City, US");
		personDTO.setGender("Male");
	}

	private void mockPerson(String change) {
		personDTO.setFirstName("Adam" + change);
		personDTO.setLastName("Sandler" + change);
		personDTO.setAddress("New York City, US" + change);
		personDTO.setGender("Male");
	}

	@Test
	@Order(1)
	void authorization() {
		var credentials = new AccountCredentialsDTO("yuri.anjos", "123");

		var accessToken = given()
				.basePath("/api/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
				.contentType(TestConfigs.APPLICATION_XML)
				.accept(TestConfigs.APPLICATION_XML)
				.body(credentials)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
				.setPort(TestConfigs.SERVER_PORT)
				.setContentType(TestConfigs.APPLICATION_XML)
				.setAccept(TestConfigs.APPLICATION_XML)
				.setBasePath("/api/person/v1")
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(2)
	void createTest() throws JsonProcessingException {
		mockPerson();

		var createdPerson =
				given()
						.spec(specification)
						.body(personDTO)
						.when()
						.post()
						.then()
						.statusCode(201)
						.extract()
						.body()
						.as(PersonDTO.class);

		personDTO.setId(createdPerson.getId());

		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId() > 0);
		assertEquals(personDTO.getFirstName(), createdPerson.getFirstName());
		assertEquals(personDTO.getLastName(), createdPerson.getLastName());
		assertEquals(personDTO.getAddress(), createdPerson.getAddress());
		assertEquals(personDTO.getGender(), createdPerson.getGender());
	}

	@Test
	@Order(3)
	void findByIdTest() throws JsonProcessingException {
		mockPerson();

		var result =
				given()
						.spec(specification)
						.pathParam("id", personDTO.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonDTO.class);


		assertNotNull(result);
		assertNotNull(result.getId());
		assertTrue(result.getId() > 0);
		assertEquals("Adam", result.getFirstName());
		assertEquals("Sandler", result.getLastName());
		assertEquals("New York City, US", result.getAddress());
		assertEquals("Male", result.getGender());
	}

	@Test
	@Order(4)
	void update() throws JsonProcessingException {
		mockPerson("!");

		var result =
				given()
						.spec(specification)
						.pathParam("id", personDTO.getId())
						.body(personDTO)
						.when()
						.put("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonDTO.class);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertTrue(result.getId() > 0);
		assertEquals("Adam!", result.getFirstName());
		assertEquals("Sandler!", result.getLastName());
		assertEquals("New York City, US!", result.getAddress());
		assertEquals("Male", result.getGender());
	}

	@Test
	@Order(5)
	void delete() {
		given()
				.spec(specification)
				.pathParam("id", personDTO.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204)
				.extract()
				.body()
				.asString();
	}
}
