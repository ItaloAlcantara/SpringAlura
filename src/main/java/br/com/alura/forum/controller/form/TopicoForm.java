package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@lombok.Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicoForm {

    @NotNull @NotEmpty @Length(min=5)
    private String titulo;
    @NotNull @NotEmpty @Length(min=10)
    private String mensagem;
    @NotNull @NotEmpty
    private String nomeCurso;

    public Topico converter(CursoRepository repository) {
        Curso curso = repository.findByNome(nomeCurso);
        return new Topico(titulo,mensagem,curso);
    }
}
