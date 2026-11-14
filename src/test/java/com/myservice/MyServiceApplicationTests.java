package com.myservice;

import org.junit.jupiter.api.Test;

/**
 * Smoke placeholder. Full {@code @SpringBootTest} needs Postgres, Redis, Keycloak, and crypto.
 * Domain/security behavior is covered by unit tests under {@code application/service} and
 * {@code infrastructure/config/security}.
 */
class MyServiceApplicationTests {

	@Test
	void projectCompilesAndUnitTestsRunInCi() {
		// Intentionally no Spring context — see ExternalAccountServiceTest / KeycloakJwtAuthenticationConverterTest
	}
}
