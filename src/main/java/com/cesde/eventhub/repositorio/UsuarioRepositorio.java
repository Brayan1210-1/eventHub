package com.cesde.eventhub.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.modelos.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID>{

	public Optional<Usuario> findByNombre(String nombre);

	public Optional<Usuario> findByEmail(String email);

	public boolean existsByEmail(String email);

	public boolean existsByDocumento(String documento);

}
