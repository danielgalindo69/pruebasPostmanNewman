package com.ejemploPostman.demo.repository;

import com.ejemploPostman.demo.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
