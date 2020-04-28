package br.com.alura.forum.config.seguranca;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    public AutenticacaoViaTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(httpServletRequest);

        boolean valido = tokenService.isValido(token);

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private String recuperarToken(HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");

        if(token==null || token.isEmpty() || !token.startsWith("Bearer"))
            return null;
        return token.substring(7,token.length());

    }
}
