package br.com.avaliacao.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.avaliacao.repository.ColaboradorRepository;

//@Entity(name="colaborador")
public class Colaborador implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="Nome é obrigatório")
	@Length(message="Digite um nome com até 50 caracteres", max=50)
	private String nome;
	
	@NotNull(message="Hierarquia é obrigatória")
	//@Enumerated(EnumType.STRING)
	private Hierarquia hierarquia;
	
	//@ManyToOne
	private Colaborador superior;

	//@OneToMany(fetch = FetchType.EAGER)
	//@JoinColumn(name = "colaboradorId")
	private List<CampoAvaliacao> campoAvaliacoes = new ArrayList<>();
	
	@NotBlank(message="Login é obrigatória")
	//@Column(unique = true)
	private String login;
	
	@NotBlank(message="Senha é obrigatória")
	private String senha;
	
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

	public Hierarquia getHierarquia(){
		return hierarquia;
	}

	public void setHierarquia(Hierarquia hierarquia){
		this.hierarquia = hierarquia;
	}

	public Colaborador getSuperior(){
		return superior;
	}

	public void setSuperior(Colaborador superior){
		this.superior = superior;
	}
	
	public void setCampoAvaliacoes(List<CampoAvaliacao> campoAvaliacoes) {
		this.campoAvaliacoes = campoAvaliacoes;
	}
	
	public List<CampoAvaliacao> getCampoAvaliacoes() {
		return campoAvaliacoes;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String avaliacao() {
		try {
			DecimalFormat formatador = new DecimalFormat();
			formatador.applyPattern("#,##0.00");
			
			return formatador.format(campoAvaliacoes.stream()
					.filter(CA -> CA.getValor() > 0)
					.mapToDouble(CampoAvaliacao::getValor)
					.average()
					.getAsDouble());
		} catch (NoSuchElementException e) {
			return "Não avaliado(a)";
		} catch (Exception e) {
			return "Erro";
		}
	}

	public static Colaborador getColaboradorLogado() {
		return (Colaborador) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@Override
	public String toString() {
		return "Colaborador [id=" + id + ", nome=" + nome + ", hierarquia=" + hierarquia + ", superior=" + superior.nome
				+ ", campoAvaliacoes=" + campoAvaliacoes + ", login=" + login + ", senha=" + senha + "]";
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Colaborador)) return false;
		Colaborador oColaborador = (Colaborador) o;

		return id == oColaborador.id;
	}
	
	public static Colaborador novo() {
		Colaborador novo = new Colaborador();
		novo.setId(ColaboradorRepository.geraId());

		// Prazo
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Executa suas atividades nos prazos acordados, sempre cumprindo todas as normas éticas."));
		
		//Custo
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Utiliza de forma adequada e cuidadosa os recursos disponíveis na Empresa."));
		
		//Qualidade
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Mostra-se atento e executa seu trabalho com agilidade e precisão, contribuindo para atingir os objetivos da qualidade da sua área."));
		
		// Capacidade de Pensar em Impactos
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Toma as providências necessárias para evitar a reincidência de um erro."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("É ciente dos objetivos da Empresa, estabelecendo prioridades e planejando ações para atingi-los."));
		
		//Competência
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Domina os conhecimentos necessários para realizar seu trabalho."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Busca caminhos alternativos quando se esgotam os meios convencionais de solução, respeitando as normas vigentes."));
		
		//Trabalho em Equipe
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Comunica-se e trabalha em equipe com facilidade."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Busca a colaboração e o comprometimento do grupo em prol de objetivos comuns."));
		
		//Comunicação
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Aceita sugestões e críticas visando o seu desenvolvimento."));	
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Expõe as suas ideias e pontos de vista com clareza, objetividade e segurança."));	
		
		//Postura
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Sabe lidar com ação do tipo imediata diante de contratempos."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Ao se deparar com uma dificuldade no trabalho, participa prontamente da busca de soluções."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Age com autonomia e responde pelos riscos assumidos e resultados atingidos."));
		
		//Responsabilidade
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Não falta ou chega atrasado no trabalho."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Assume suas obrigações e deveres de forma plena, mesmo que nas situações difíceis."));
		
		//Relacionamento
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Estabelece bom relacionamento com chefia e colegas de trabalho."));
		
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Possui equilíbrio emocional em situações de conflito, tratando pessoas de forma respeitosa, contornando com diplomacia situações instaladas."));
		
		//Cultura da Empresa
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Se enquadra na cultura da empresa."));
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Demonstra interesse em participar das atividades de capacitação e qualificação profissional promovidas pela Instituição, através dos treinamentos e cursos oferecidos internamente."));
		
		//Missão
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Caminha junto a missão da empresa."));
		
		//Valores
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Caminha junto aos valores da empresa."));
		
		//Normas
		novo.campoAvaliacoes.add(CampoAvaliacao.nova("Executa ações e processos de trabalho de acordo com as normas e procedimentos da empresa."));
		
		return novo;
	}
	
	@Override
	public String getUsername() {
		return nome;
	}
	
	@Override
	public String getPassword() {
		return senha;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
