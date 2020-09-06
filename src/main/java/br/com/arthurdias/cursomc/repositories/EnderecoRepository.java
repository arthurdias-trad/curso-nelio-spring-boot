package br.com.arthurdias.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.arthurdias.cursomc.domain.Endereco;

@Repository


public interface EnderecoRepository extends JpaRepository<Endereco, Integer>{
		
}
