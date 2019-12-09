package br.com.avaliacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.avaliacao.entity.Colaborador;
import br.com.avaliacao.entity.Hierarquia;
import br.com.avaliacao.repository.ColaboradorRepository;

@Service
public class ColaboradorService implements UserDetailsService{

	@Autowired
	private ColaboradorRepository repository;

	public List<Colaborador> findAll() {
		return repository.findAll();
	}

	public Colaborador save(Colaborador colaborador) {
		return repository.save(colaborador);
	}

	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	public Colaborador findById(Integer id) {
		Optional<Colaborador> optional = repository.findById(id);
		if(!optional.isPresent()) return null;

		return optional.get();
	}

	public List<Colaborador> findAllByHierarquiaOrderByNome(Hierarquia hierarquia){
		return repository.findAllByHierarquiaOrderByNome(hierarquia);
	}

	public List<Colaborador> findAllBySuperiorOrderByNome(Colaborador superior){
		return repository.findAllBySuperiorOrderByNome(superior);
	}

	@Override
	public Colaborador loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Colaborador> lista = repository.findAllByLogin(username);
		
		if(!lista.isEmpty()) return lista.get(0);
		
		throw new UsernameNotFoundException("Funcionário de login: " + username + " não foi encontrado"); 
	}
}
