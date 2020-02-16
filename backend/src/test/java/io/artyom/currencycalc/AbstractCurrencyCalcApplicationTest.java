package io.artyom.currencycalc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneOffset;
import java.util.TimeZone;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractCurrencyCalcApplicationTest {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
	}

	//TODO should be moved to separate library: "ApiClient"
	public String getApiURI(){
		return "http://127.0.0.1:" + port + "/api/adm/v1/";
	}

	@LocalServerPort
	protected int port;

	@Autowired
	protected TestRestTemplate restTemplate;

}
