package br.com.avaliacao.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import br.com.avaliacao.entity.CampoAvaliacao;
import br.com.avaliacao.entity.Colaborador;
 
@Component
public class ValidacaoService {
 
    public void validar(Colaborador colaborador, BindingResult result) {
    	for (CampoAvaliacao campoAvaliacao : colaborador.getCampoAvaliacoes()){
    		if(campoAvaliacao.getValor() < 0 || campoAvaliacao.getValor() > 5) {
    			result.rejectValue("campoAvaliacoes", "Range", new Object[]{"'campoAvaliacoes"}, 
        				"Escolha valores entre 0 e 5");
    			break;
    		}
    	}
    }
}

