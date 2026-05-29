package com.ejemploPostman.demo.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ejemploPostman.demo.entity.Persona;
import com.ejemploPostman.demo.service.PersonaService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonaControllerTest {

	@LocalServerPort
	private int port;

	@MockitoBean
	private PersonaService personaService;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void crearPersonaRetornaCreado() {
		when(personaService.crear(any(Persona.class)))
				.thenReturn(new Persona(1L, "Juan", "Perez", "3001234567"));

		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("""
						{
						  "nombre": "Juan",
						  "apellido": "Perez",
						  "numero": "3001234567"
						}
						""")
				.when()
				.post("/api/personas")
				.then()
				.statusCode(201)
				.body("id", equalTo(1))
				.body("nombre", equalTo("Juan"))
				.body("apellido", equalTo("Perez"))
				.body("numero", equalTo("3001234567"));
	}

	@Test
	void crearPersonaInvalidaRetornaBadRequest() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body("""
						{
						  "nombre": "J",
						  "apellido": "P",
						  "numero": "abc"
						}
						""")
				.when()
				.post("/api/personas")
				.then()
				.statusCode(400)
				.body("nombre", equalTo("El nombre debe tener entre 2 y 60 caracteres"))
				.body("apellido", equalTo("El apellido debe tener entre 2 y 60 caracteres"))
				.body("numero", equalTo("El numero debe contener entre 7 y 15 digitos"));
	}
}
