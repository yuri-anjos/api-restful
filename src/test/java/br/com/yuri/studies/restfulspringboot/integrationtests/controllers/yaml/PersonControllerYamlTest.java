package br.com.yuri.studies.restfulspringboot.integrationtests.controllers.yaml;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.controllers.yaml.mapper.YamlMapper;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.TokenDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.wrappers.PagedModelPerson;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
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
class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static PersonDTO personDTO;
	private static YamlMapper mapper;

	@BeforeAll
	public static void setUp() {
		mapper = new YamlMapper();
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
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.APPLICATION_YAML,
												ContentType.TEXT)))
				.contentType(TestConfigs.APPLICATION_YAML)
				.accept(TestConfigs.APPLICATION_YAML)
				.body(credentials, mapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class, mapper)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
				.setPort(TestConfigs.SERVER_PORT)
				.setConfig(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.APPLICATION_YAML,
												ContentType.TEXT)))
				.setContentType(TestConfigs.APPLICATION_YAML)
				.setAccept(TestConfigs.APPLICATION_YAML)
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
						.body(personDTO, mapper)
						.when()
						.post()
						.then()
						.statusCode(201)
						.extract()
						.body()
						.as(PersonDTO.class, mapper);

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
	void testFindAll() {
		var wrapper = given().spec(specification)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, mapper);

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
						.body(personDTO, mapper)
						.when()
						.put("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonDTO.class, mapper);

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
						.as(PersonDTO.class, mapper);

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

		assertTrue(content.contains("- rel: \"first\"\n  href: \"http://localhost:8888/api/person/v1?sortBy=firstName&sortDirection=ASC&page=0&size=10&sort=firstName,asc\"\n"));
		assertTrue(content.contains("- rel: \"prev\"\n  href: \"http://localhost:8888/api/person/v1?sortBy=firstName&sortDirection=ASC&page=2&size=10&sort=firstName,asc\"\n"));
		assertTrue(content.contains("- rel: \"self\"\n  href: \"http://localhost:8888/api/person/v1?page=3&size=10&sortBy=firstName&sortDirection=ASC\"\n"));
		assertTrue(content.contains("- rel: \"next\"\n  href: \"http://localhost:8888/api/person/v1?sortBy=firstName&sortDirection=ASC&page=4&size=10&sort=firstName,asc\"\n"));
		assertTrue(content.contains("- rel: \"last\"\n  href: \"http://localhost:8888/api/person/v1?sortBy=firstName&sortDirection=ASC&page=100&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("page:\n  size: 10\n  totalElements: 1005\n  totalPages: 101\n  number: 3"));

	}
}
