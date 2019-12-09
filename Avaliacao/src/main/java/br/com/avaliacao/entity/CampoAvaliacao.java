package br.com.avaliacao.entity;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import br.com.avaliacao.repository.ColaboradorRepository;

//@Entity(name="campoavaliacao")
public class CampoAvaliacao implements Serializable {
	private static final long serialVersionUID = 1L;

	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Length(message="Digite um nome com até 2000 caracteres", max=2000)
	private String nome;
	
	//@ColumnDefault("0")
	@Range(min=0,max=5, message="Escolha valores entre 0 e 5")
	private int valor;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getNome(){
		return nome;
	}

	public void setNome(String nome){
		this.nome = nome;
	}

	public int getValor(){
		return valor;
	}

	public void setValor(int valor){
		this.valor = valor;
	}

	public static CampoAvaliacao nova(String nome) {
		CampoAvaliacao nova = new CampoAvaliacao();
		nova.setId(ColaboradorRepository.geraId());
		nova.setNome(nome);
		
		return nova;
	}
	
	@Override
	public String toString() {
		return "CampoAvaliacao [id=" + id + ", nome=" + nome + ", valor=" + valor + "]";
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof CampoAvaliacao)) return false;
		CampoAvaliacao oCampoAvaliacao = (CampoAvaliacao) o;

		return id == oCampoAvaliacao.id;
	}
}
