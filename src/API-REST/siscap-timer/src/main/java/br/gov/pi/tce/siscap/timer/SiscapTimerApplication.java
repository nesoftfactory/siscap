package br.gov.pi.tce.siscap.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty;

@SpringBootApplication
@EnableConfigurationProperties(SiscapTimerProperty.class)
public class SiscapTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiscapTimerApplication.class, args);
	}

}

