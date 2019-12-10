package br.com.avaliacao.entity;

import java.util.Arrays;
import java.util.List;

public enum Hierarquia {
	diretor, superintendente, coordenador, funcionario;

	public String imprime() {
		switch (this) {
			case diretor: return "Diretor(a)";
			case superintendente: return "Superintendente";
			case coordenador: return "Coordenador(a)";
			case funcionario: return "Funcionario(a)";
			default: return null;
		}
	}
	
	public List<Hierarquia> listaDeCadastroPermitida() {
		switch (this) {
			case diretor: return Arrays.asList(diretor, superintendente);
			case superintendente: return Arrays.asList(coordenador);
			case coordenador: return Arrays.asList(funcionario);
			case funcionario: return Arrays.asList();
			default: return null;
		}
	}
}
