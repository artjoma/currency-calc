package io.artyom.currencycalc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class CurrencyCalcApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		log.info("Set default application Timezone: {}", TimeZone.getDefault().getID());

		System.setProperty("sun.net.client.defaultConnectTimeout", "3000");
		System.setProperty("sun.net.client.defaultReadTimeout", "3000");

		SpringApplication.run(CurrencyCalcApplication.class, args);
	}

}
