package br.com.alura.forum.config.seguranca;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AutenticacaoService autencicacaoService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] endpointsGet = {"/topicos","/topicos/*","/actuator/**"};
        String[] endpointsPost= {"/auth"};
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,endpointsGet).permitAll()
                .antMatchers(HttpMethod.POST,endpointsPost).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AutenticacaoViaTokenFilter(tokenService,usuarioRepository),UsernamePasswordAuthenticationFilter.class);
    }

    //Configurações de autencicação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autencicacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configuração de recursos estaticos (JS,CSS,IMG e ETC...)
    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/**.html","/v2/api-docs","/webjars/**","/configuration/**","/swagger-resources/**");
    }
}
