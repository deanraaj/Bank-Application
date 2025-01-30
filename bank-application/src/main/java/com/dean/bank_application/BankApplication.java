package com.dean.bank_application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The My Bank App ",
				description = "The Backend REST apis for My Bank App",
				version = "v1.0",
				contact = @Contact(
						name = "Nagaraju Dola",
						email = "nagarajudola125@gmail.com",
						url = "https://github.com/deanraaj/Bank-Application"
				),
				license = @License(
						name = "nagaraju_dola",
						url = "https://github.com/deanraaj/Bank-Application"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The My Bank",
				url = "/"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
