package br.com.arthurdias.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
	
	public Page<CategoriaDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		System.out.println(Direction.valueOf(direction));
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<Categoria> pageCategorias = this.repo.findAll(pageRequest);
		Page<CategoriaDTO> pageDTO = pageCategorias.map(obj -> new CategoriaDTO(obj));
		
		return pageDTO;
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
}
