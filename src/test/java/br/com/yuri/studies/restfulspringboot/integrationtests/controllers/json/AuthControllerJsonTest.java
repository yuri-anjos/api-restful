package br.com.yuri.studies.restfulspringboot.integrationtests.controllers.json;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.AccountCredentialsDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.TokenDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.dtos.UserDTO;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
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
public class AuthControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static UserDTO userDTO;
	private static AccountCredentialsDTO accountCredentialsDTO;
	private static String refreshToken;

	@BeforeAll
	public static void setUp() {
		userDTO = new UserDTO();
		accountCredentialsDTO = new AccountCredentialsDTO();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_YURI)
				.setContentType(TestConfigs.APPLICATION_JSON)
				.setAccept(TestConfigs.APPLICATION_JSON)
				.setPort(TestConfigs.SERVER_PORT)
				.setBasePath("/api/auth")
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}

	private void mockUser() {
		userDTO.setUsername("test.json");
		userDTO.setFullname("test one");
		userDTO.setPassword("123pass");
		userDTO.setConfirmPassword("123pass");

		accountCredentialsDTO.setUsername("test.json");
		accountCredentialsDTO.setPassword("123pass");
	}

	@Test
	@Order(1)
	void signupTest() {
		mockUser();

		var tokenDTO = given()
				.spec(specification)
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
	@Order(2)
	void signinTest() {
		mockUser();

		var tokenDTO = given()
				.spec(specification)
				.body(accountCredentialsDTO)
				.when()
				.post("/signin")
				.then()
				.statusCode(200)
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

		refreshToken = tokenDTO.getRefreshToken();
	}

	@Test
	@Order(3)
	void refreshTokenTest() {
		mockUser();

		var tokenDTO = given()
				.spec(specification)
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + refreshToken)
				.when()
				.post("/refresh")
				.then()
				.statusCode(200)
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
}
