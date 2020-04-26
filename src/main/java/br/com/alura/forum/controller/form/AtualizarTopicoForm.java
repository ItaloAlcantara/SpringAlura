package br.com.alura.forum.controller.form;

import br.com.alura.forum.config.ExceptionMessage;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarTopicoForm {

    private String mensagem;
    private String titulo;

    public Topico atualizer(Long id, TopicoRepository topicoRepository) throws ExceptionMessage {
        if(!topicoRepository.findById(id).isPresent()){
            throw new ExceptionMessage();
        }
        Topico topico = topicoRepository.getOne(id);

        topico.setMensagem(this.mensagem);
        topico.setTitulo(this.titulo);
        return topico;
    }
}
