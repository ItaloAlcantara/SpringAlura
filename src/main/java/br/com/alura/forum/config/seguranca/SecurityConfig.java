package br.com.alura.forum.config.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AutenticacaoService autencicacaoService;

    //Configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] endpoints = {"/topicos","/topicos/*"};
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,endpoints).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }

    //Configurações de autencicação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autencicacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configuração de recursos estaticos (JS,CSS,IMG e ETC...)
    @Override
    public void configure(WebSecurity web) throws Exception {

    }
}
