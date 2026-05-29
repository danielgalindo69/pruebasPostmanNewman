package com.ejemploPostman.demo.service;

import com.ejemploPostman.demo.entity.Persona;
import com.ejemploPostman.demo.repository.PersonaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

	private final PersonaRepository personaRepository;

	public PersonaService(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	public List<Persona> listar() {
		return personaRepository.findAll();
	}

	public Persona buscarPorId(Long id) {
		return personaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No existe una persona con id " + id));
	}

	public Persona crear(Persona persona) {
		return personaRepository.save(persona);
	}

	public Persona actualizar(Long id, Persona persona) {
		Persona personaExistente = buscarPorId(id);
		personaExistente.setNombre(persona.getNombre());
		personaExistente.setApellido(persona.getApellido());
		personaExistente.setNumero(persona.getNumero());
		return personaRepository.save(personaExistente);
	}

	public void eliminar(Long id) {
		Persona persona = buscarPorId(id);
		personaRepository.delete(persona);
	}
}
