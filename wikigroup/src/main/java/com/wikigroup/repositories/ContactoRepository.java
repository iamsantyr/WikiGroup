package com.wikigroup.repositories;

import com.wikigroup.models.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {}
