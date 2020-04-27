package br.com.alura.forum.config.seguranca;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication){

        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date expiracao= new Date(hoje.getTime()+Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("Api Curso Spring")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }
}
