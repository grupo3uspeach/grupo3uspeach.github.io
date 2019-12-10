package br.com.avaliacao.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.avaliacao.entity.CampoAvaliacao;
import br.com.avaliacao.entity.Colaborador;
import br.com.avaliacao.entity.Hierarquia;
import br.com.avaliacao.service.ColaboradorService;
import br.com.avaliacao.service.ValidacaoService;

@Controller
public class AplicacaoController {

	@Autowired
	private ColaboradorService colaboradorService;

	@Autowired
	private ValidacaoService validacaoService;
	
	@RequestMapping("/")
	public ModelAndView home() {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		return new ModelAndView("home")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()));
	}
	
	@GetMapping("/login")
	public ModelAndView login(@RequestParam(value="error", required=false) String error) {
		return new ModelAndView("login").addObject("erro", error);
	}
	
	@RequestMapping("form") // mapemento da url
	public ModelAndView form() {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		return new ModelAndView("form") // nome do arquivo html
			.addObject("colaborador", colaborador)
			.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
			.addObject("novo", Colaborador.novo()) // objeto ${objeto} usado no html -> nome objeto
			.addObject("hierarquias", colaborador.getHierarquia().listaDeCadastroPermitida());
	}

	@RequestMapping("salvar")
	public ModelAndView salvar(@Valid @ModelAttribute("novo") Colaborador novo, BindingResult result) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		if(Hierarquia.diretor != novo.getHierarquia()) novo.setSuperior(colaboradorService.findById(colaborador.getId()));
		
		validacaoService.validar(novo, result); // minha validacao
		
		if(result.hasErrors()) { // validacao do spring
			// se der erro, retorna o mesmo objeto
			return  new ModelAndView("form")
					.addObject("colaborador", colaborador)
					.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
					.addObject("novo", novo)
					.addObject("hierarquias", colaborador.getHierarquia().listaDeCadastroPermitida());
		}
		
		// se nao der erro, salva tudo
		colaboradorService.save(novo);
		
		// redireciona para a url /form
		return new ModelAndView("redirect:/form");
	}

	@RequestMapping("subordinados")
	public ModelAndView lista() {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		return new ModelAndView("subordinados")
			.addObject("colaborador", colaborador)
			.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
			.addObject("subordinados", colaboradorService.findAllBySuperiorOrderByNome(colaboradorService.findById(colaborador.getId())));
	}

	@RequestMapping("alterar")
	public ModelAndView alterar(@RequestParam("subordinado") Colaborador subordinado) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		return new ModelAndView("form")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
				.addObject("novo", subordinado)
				.addObject("hierarquias", colaborador.getHierarquia().listaDeCadastroPermitida());
	}

	@RequestMapping("avaliar")
	public ModelAndView avaliar(@RequestParam("subordinado") Colaborador subordinado) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		return new ModelAndView("avaliar")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
				.addObject("subordinado", subordinado);
	}
	
	@RequestMapping("excluir")
	public ModelAndView excluir(@RequestParam("subordinado") Colaborador subordinado) {
		colaboradorService.deleteById(subordinado.getId());
		return lista();
	}
	
	@RequestMapping("avaliar/salvar")
	public ModelAndView avaliarSalvar(@Valid @ModelAttribute("subordinado") Colaborador subordinado, BindingResult result) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		validacaoService.validar(subordinado, result);
		if(result.hasErrors()) {
			return  new ModelAndView("avaliar")
					.addObject("colaborador", colaborador)
					.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
					.addObject("subordinado", subordinado);
		}
		
		subordinado = colaboradorService.save(subordinado);
		
		return new ModelAndView("avaliar")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
				.addObject("subordinado", subordinado);
	}
	
	@RequestMapping("avaliar/novo")
	public ModelAndView avaliaNovo(@ModelAttribute("subordinado") Colaborador subordinado) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		subordinado.getCampoAvaliacoes().add(CampoAvaliacao.nova(null));
		
		return new ModelAndView("avaliar")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
				.addObject("subordinado", subordinado);
	}
	
	@RequestMapping("avaliar/excluir")
	public ModelAndView avaliaExcluir(@RequestParam("subordinado") Colaborador subordinado, @RequestParam("campoavaliacao_id") Integer campoavaliacao_id) {
		Colaborador colaborador = Colaborador.getColaboradorLogado();
		
		CampoAvaliacao remove = new CampoAvaliacao();
		remove.setId(campoavaliacao_id);
		subordinado.getCampoAvaliacoes().remove(remove);
		colaboradorService.save(subordinado);
		
		return new ModelAndView("avaliar")
				.addObject("colaborador", colaborador)
				.addObject("isDiretor", Hierarquia.diretor.equals(colaborador.getHierarquia()))
				.addObject("subordinado", subordinado);
	}
}
