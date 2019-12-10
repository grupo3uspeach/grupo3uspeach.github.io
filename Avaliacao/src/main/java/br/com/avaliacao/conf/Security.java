package br.com.avaliacao.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.avaliacao.service.ColaboradorService;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			//.permitAll()
			.authenticated()
			.and()
				.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/")
			.and()
				.logout()
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
	            .logoutSuccessUrl("/login");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(colaboradorService)
				.passwordEncoder(new PasswordEncoder() {
					@Override
					public String encode(CharSequence rawPassword) {
						return String.valueOf(rawPassword);
					}

					@Override
					public boolean matches(CharSequence rawPassword, String encodedPassword) {
						return String.valueOf(rawPassword).equals(encodedPassword);
					}
				});
	}
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/css/**", "/imagens/**", "/webjars/**");
    }
}
