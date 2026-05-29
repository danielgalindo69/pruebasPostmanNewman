package com.ejemploPostman.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "personas")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 2, max = 60, message = "El nombre debe tener entre 2 y 60 caracteres")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	@Size(min = 2, max = 60, message = "El apellido debe tener entre 2 y 60 caracteres")
	private String apellido;

	@NotBlank(message = "El numero es obligatorio")
	@Pattern(regexp = "\\d{7,15}", message = "El numero debe contener entre 7 y 15 digitos")
	private String numero;

	public Persona() {
	}

	public Persona(Long id, String nombre, String apellido, String numero) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
