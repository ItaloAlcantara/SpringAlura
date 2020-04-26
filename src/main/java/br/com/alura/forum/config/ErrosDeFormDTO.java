package br.com.alura.forum.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrosDeFormDTO {

    private String campo;
    private String erro;
}
