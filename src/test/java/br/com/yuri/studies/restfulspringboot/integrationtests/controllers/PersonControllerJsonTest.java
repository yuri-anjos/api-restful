package br.com.yuri.studies.restfulspringboot.integrationtests.controllers;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonDTO personDTO;

	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
	void createTest() throws JsonProcessingException {
		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.body(personDTO)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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
	@Order(2)
	void createInvalidOriginTest() {
		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.body(personDTO)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	void findByIdTest() throws JsonProcessingException {
		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.pathParam("id", personDTO.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonDTO result = objectMapper.readValue(content, PersonDTO.class);
		personDTO = result;

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
	void findByIdInvalidOriginTest() {
		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.pathParam("id", personDTO.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(5)
	void update() throws JsonProcessingException {
		mockPerson("!");

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.pathParam("id", personDTO.getId())
						.body(personDTO)
						.when()
						.put("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonDTO result = objectMapper.readValue(content, PersonDTO.class);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertTrue(result.getId() > 0);
		assertEquals("Adam!", result.getFirstName());
		assertEquals("Sandler!", result.getLastName());
		assertEquals("New York City, US!", result.getAddress());
		assertEquals("Male", result.getGender());
	}

	@Test
	@Order(6)
	void updateInvalidOrigin() {
		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.pathParam("id", personDTO.getId())
						.when()
						.put("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(7)
	void delete() {
		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		given()
				.spec(specification)
				.contentType(TestConfigs.APPLICATION_JSON)
				.pathParam("id", personDTO.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204)
				.extract()
				.body()
				.asString();
	}

	@Test
	@Order(8)
	void deleteInvalidOrigin() {
		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		var content =
				given()
						.spec(specification)
						.contentType(TestConfigs.APPLICATION_JSON)
						.pathParam("id", personDTO.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
}
