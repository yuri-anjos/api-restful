package br.com.yuri.studies.restfulspringboot.integrationtests.controllers;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.TokenDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.UserDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CorsTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static UserDTO userDTO;

	@BeforeAll
	public static void setUp() {

		specification = new RequestSpecBuilder()
				.setContentType(TestConfigs.APPLICATION_JSON)
				.setBasePath("/api/auth")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}

	private void mockUser() {
		userDTO = new UserDTO();
		userDTO.setUsername("test.test");
		userDTO.setFullname("test one");
		userDTO.setPassword("123pass");
		userDTO.setConfirmPassword("123pass");
	}

	@Test
	void signupTest() {
		mockUser();

		var tokenDTO = given()
				.spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
				.body(userDTO)
				.when()
				.post("/signup")
				.then()
				.statusCode(201)
				.extract()
				.body()
				.as(TokenDTO.class);

		assertNotNull(tokenDTO);
		assertNotNull(tokenDTO.getAccessToken());
		assertNotNull(tokenDTO.getRefreshToken());
		assertNotNull(tokenDTO.getExpiration());
		assertNotNull(tokenDTO.getCreated());
		assertTrue(tokenDTO.getAuthenticated());
		assertEquals(userDTO.getUsername(), tokenDTO.getUsername());
	}

	@Test
	void signupInvalidCorsTest() {
		mockUser();

		var tokenDTO = given()
				.spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
				.body(userDTO)
				.when()
				.post("/signup")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(tokenDTO);
		assertEquals("Invalid CORS request", tokenDTO);
	}
}
