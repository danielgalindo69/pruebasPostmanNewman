package com.ejemploPostman.demo.controller;

import com.ejemploPostman.demo.entity.Persona;
import com.ejemploPostman.demo.service.PersonaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

	private final PersonaService personaService;

	public PersonaController(PersonaService personaService) {
		this.personaService = personaService;
	}

	@GetMapping
	public ResponseEntity<List<Persona>> listar() {
		return ResponseEntity.ok(personaService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(personaService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<Persona> crear(@Valid @RequestBody Persona persona) {
		Persona personaCreada = personaService.crear(persona);
		return ResponseEntity
				.created(URI.create("/api/personas/" + personaCreada.getId()))
				.body(personaCreada);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Persona> actualizar(@PathVariable Long id, @Valid @RequestBody Persona persona) {
		return ResponseEntity.ok(personaService.actualizar(id, persona));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		personaService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
