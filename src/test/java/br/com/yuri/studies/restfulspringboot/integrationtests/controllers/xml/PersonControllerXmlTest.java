package br.com.yuri.studies.restfulspringboot.integrationtests.controllers.xml;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.TokenDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.wrappers.PagedModelPerson;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static PersonDTO personDTO;
	private static ObjectMapper objectMapper;

	@BeforeAll
	public static void setUp() {
		objectMapper = new XmlMapper();
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
	void createTest() {
		mockPerson();

		var result =
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

		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertTrue(result.getEnabled());

		personDTO = result;
	}

	@Test
	@Order(3)
	void testFindAll() throws JsonProcessingException {
		var content = given().spec(specification)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		var wrapper = objectMapper.readValue(content, PagedModelPerson.class);
		var people = wrapper.getContent();
		assertNotNull(people.get(0));
		assertEquals(12, people.size());
	}

	@Test
	@Order(4)
	void update() {
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
		assertEquals(personDTO.getId(), result.getId());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertTrue(result.getEnabled());

		personDTO = result;
	}

	@Test
	@Order(5)
	void disable() {
		given()
				.spec(specification)
				.when()
				.patch("/{id}/disable", personDTO.getId())
				.then()
				.statusCode(204);
	}

	@Test
	@Order(6)
	void findByIdTest() {
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
		assertEquals(personDTO.getId(), result.getId());
		assertEquals(personDTO.getFirstName(), result.getFirstName());
		assertEquals(personDTO.getLastName(), result.getLastName());
		assertEquals(personDTO.getAddress(), result.getAddress());
		assertEquals(personDTO.getGender(), result.getGender());
		assertFalse(result.getEnabled());
	}

	@Test
	@Order(7)
	void delete() {
		given()
				.spec(specification)
				.when()
				.delete("/{id}", personDTO.getId())
				.then()
				.statusCode(204);
	}

	@Test
	@Order(8)
	void testFindAllHateoas()  {
		var content = given().spec(specification)
				.queryParams("page", 3, "size", 10)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?sortBy=firstName&amp;sortDirection=ASC&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?sortBy=firstName&amp;sortDirection=ASC&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost:8888/api/person/v1?sortBy=firstName&amp;sortDirection=ASC&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1?page=3&amp;size=10&amp;sortBy=firstName&amp;sortDirection=ASC</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/person/v1?sortBy=firstName&amp;sortDirection=ASC&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/person/v1?sortBy=firstName&amp;sortDirection=ASC&amp;page=100&amp;size=10&amp;sort=firstName,asc</href>"));
		assertTrue(content.contains("<page><size>10</size><totalElements>1005</totalElements><totalPages>101</totalPages><number>3</number></page>"));
	}
}
