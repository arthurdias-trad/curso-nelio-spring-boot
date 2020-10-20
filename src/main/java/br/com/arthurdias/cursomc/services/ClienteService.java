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

import br.com.arthurdias.cursomc.domain.Cliente;
import br.com.arthurdias.cursomc.dto.ClienteDTO;
import br.com.arthurdias.cursomc.repositories.ClienteRepository;
import br.com.arthurdias.cursomc.services.exceptions.DataIntegrityException;
import br.com.arthurdias.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " +  id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = this.find(obj.getId());
		UpdateData(newObj, obj);		
		
		return this.repo.save(newObj);
	}
	
	public void delete(Integer id) {
		this.find(id);
		
		try {
			this.repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que tem pedidos cadastrados");
		}
	}
	
	public List<ClienteDTO> findAll() {
		List<Cliente> categorias = this.repo.findAll();
		List<ClienteDTO> listaDTO = categorias
				.stream()
				.map(obj -> new ClienteDTO(obj))
				.collect(Collectors.toList());
		
		return listaDTO;
	}
	
	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		System.out.println(Direction.valueOf(direction));
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<Cliente> pageClientes = this.repo.findAll(pageRequest);
		Page<ClienteDTO> pageDTO = pageClientes.map(obj -> new ClienteDTO(obj));
		
		return pageDTO;
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	public void UpdateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
