package br.com.avaliacao.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.avaliacao.entity.Colaborador;
import br.com.avaliacao.entity.Hierarquia;

@Component
public class ColaboradorRepository {
	
	private static Map<Integer, Colaborador> map;
	private static int geraId;
	private static Comparator<Colaborador> comparator;
	
	static {
		map = new TreeMap<>();
		geraId = 0;
		comparator = new Comparator<Colaborador>() {
			@Override
			public int compare(Colaborador o1, Colaborador o2) {
				if(o1.getNome() == null && o2.getNome() == null) return 0;
				if(o1.getNome() == null) return 1;
				if(o2.getNome() == null) return -1;
				
				return o1.getNome().compareTo(o2.getNome());
			}
		};
		
		// Salvando o ADM
		Colaborador colaborador = Colaborador.novo();
		colaborador.setId(geraId());
		colaborador.setNome("ADM");
		colaborador.setHierarquia(Hierarquia.diretor);
		colaborador.setLogin("adm");
		colaborador.setSenha("1234");
		
		map.put(colaborador.getId(), colaborador);
	}
	
	public static int geraId() {
		geraId++;
		return geraId;
	}
	
	public List<Colaborador> findAll() {
		return new ArrayList<>(map.values());
	}

	public Colaborador save(Colaborador colaborador) {
		map.remove(colaborador.getId());
		map.put(colaborador.getId(), colaborador);
		
		return colaborador;
	}

	public void deleteById(Integer id) {
		map.remove(id);
	}

	public Optional<Colaborador> findById(Integer id) {
		Colaborador colaborado = map.get(id);
		return colaborado != null ? Optional.of(colaborado) : Optional.empty();
	}
	
	public int count() {
		return map.size();
	}
	
	public List<Colaborador> findAllByHierarquiaOrderByNome(Hierarquia hierarquia) {
		return map.values().stream().filter(c -> hierarquia.equals(c.getHierarquia())).sorted(comparator)
				.collect(Collectors.toList());
	}

	public List<Colaborador> findAllBySuperiorOrderByNome(Colaborador superior){
		return map.values().stream().filter(c -> superior.equals(c.getSuperior())).sorted(comparator)
				.collect(Collectors.toList());
	}
	
	public List<Colaborador> findAllByLogin(String login){
		return map.values().stream().filter(c -> login.equals(c.getLogin()))
				.collect(Collectors.toList());
	}
}
