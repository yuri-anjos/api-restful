package br.com.yuri.studies.restfulspringboot.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	static class Initializer
			implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		private static final MySQLContainer<?> mysql =
				new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));

		private static void startContainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			MapPropertySource testcontainers =
					new MapPropertySource("testcontainers", createConnectionConfiguration());
			environment.getPropertySources().addFirst(testcontainers);
		}

		private static Map<String, Object> createConnectionConfiguration() {
			return Map.of(
					"spring.datasource.url", mysql.getJdbcUrl(),
					"spring.datasource.username", mysql.getUsername(),
					"spring.datasource.password", mysql.getPassword());
		}
	}
}
