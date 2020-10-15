package br.com.arthurdias.cursomc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.arthurdias.cursomc.domain.Categoria;
import br.com.arthurdias.cursomc.dto.CategoriaDTO;
import br.com.arthurdias.cursomc.repositories.CategoriaRepository;
import br.com.arthurdias.cursomc.services.exceptions.DataIntegrityException;
import br.com.arthurdias.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " +  id + ", Tipo: " + Categoria.class.getName()));
	}
	
	

	public Categoria insert(Categoria obj) {
		obj.setId(null);
		
		return this.repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		this.find(obj.getId());
		
		return this.repo.save(obj);
	}
	
	public void delete(Integer id) {
		this.find(id);
		
		try {
			this.repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que tem produtos cadastrados");
		}
	}
	
	public List<CategoriaDTO> findAll() {
		List<Categoria> categorias = this.repo.findAll();
		List<CategoriaDTO> listaDTO = categorias
				.stream()
				.map(obj -> new CategoriaDTO(obj))
				.collect(Collectors.toList());
		
		return listaDTO;
	}
}
