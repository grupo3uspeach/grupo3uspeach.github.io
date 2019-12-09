package br.com.avaliacao.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.avaliacao.entity.Colaborador;
import br.com.avaliacao.entity.Hierarquia;
import br.com.avaliacao.service.ColaboradorService;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new Converter<String, Hierarquia>() {

			@Override
			public Hierarquia convert(String string) {
				if(string == null || "-1".equals(string) || string.isEmpty()) return null;
				return Hierarquia.valueOf(string);
			}

		});
		
		registry.addConverter(new Converter<String, Colaborador>() {

			@Override
			public Colaborador convert(String string) {
				if (string == null || "-1".equals(string) || string.isEmpty())
					return null;
				
				return colaboradorService.findById(Integer.valueOf(string));
			}

		});
		
	}

}
