package br.com.yuri.studies.restfulspringboot.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.yuri.studies.restfulspringboot.configs.TestConfigs;
import br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerTest extends AbstractIntegrationTest {

  @Test
  void shouldDisplaySwaggerUiPage() {
    var content =
        given()
            .basePath("/api/swagger-ui/index.html")
            .port(TestConfigs.SERVER_PORT)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();
    assertTrue(content.contains("Swagger UI"));
  }
}
