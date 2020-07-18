package com.mrwhite.polls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(value = "com.mrwhite.polls", basePackageClasses = {
		Jsr310JpaConverters.class
}) //Configures the base packages used by auto-configuration when scanning for entity classes
//We’ll need to register JPA 2.1 converters so that all the Java 8 Date/Time fields in the domain models automatically get converted to SQL
// types when we persist them in the database.
public class PollsApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	} //it gets executed after the spring bean is initialized

	public static void main(String[] args) {
		SpringApplication.run(PollsApplication.class, args);
	}

}
