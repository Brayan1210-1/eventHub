package com.cesde.eventhub.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.modelos.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{

	public Usuario findByNombre(String nombre);
	
	public Usuario findByEmail(String email);

	public boolean existsByEmail(String email);
	
}
