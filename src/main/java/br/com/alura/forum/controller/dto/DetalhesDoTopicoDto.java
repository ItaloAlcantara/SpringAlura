package br.com.alura.forum.controller.dto;

import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetalhesDoTopicoDto {

    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String nomeAutor;
    private StatusTopico statusTopico;
    private List<RespostaDto> respostaDtoList;

    public DetalhesDoTopicoDto(Topico topico){
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
        this.nomeAutor = topico.getAutor().getNome();
        this.statusTopico = topico.getStatus();
        this.respostaDtoList = new ArrayList<>();
        this.respostaDtoList.addAll(topico.getRespostas()
                .stream()
                .map(RespostaDto::new)
                .collect(Collectors.toList()));
    }
}
