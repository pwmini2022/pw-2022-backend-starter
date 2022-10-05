package pw.react.backend;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import pw.react.backend.services.HttpService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(profiles = {"mysql-dev"})
class SampleBackendApplicationTests {

	@Autowired
	private HttpService httpService;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void whenConsume_thenReturnQuote() {
		try {
			String response = restTemplate.getForObject("https://api.dane.gov.pl/applications?page=1&per_page=10", String.class);
			assertThat(response).isNotNull();
		} catch (Exception ex) {
			LoggerFactory.getLogger(this.getClass()).error(ex.getMessage(), ex);
		}
	}
}
