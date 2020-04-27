package br.com.alura.forum.controller;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;

import br.com.alura.forum.config.ExceptionMessage;
import br.com.alura.forum.config.seguranca.TokenService;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;

import br.com.alura.forum.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	private final static String LISTA_TOPICOS = "listaTopicos";
    private final static String LISTA_TOPICO_DETALHADO = "listaTopicoDetalhado";

	@GetMapping
    @Cacheable(value = LISTA_TOPICOS)
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id",size = 5) org.springframework.data.domain.Pageable paginacao) {

	    System.out.println("executado");
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
			return TopicoDto.converter(topicos);
		}
	}

	@PostMapping
    @CacheEvict(value = LISTA_TOPICOS,allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){

		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri= uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
    @Cacheable(value = LISTA_TOPICO_DETALHADO)
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) throws ExceptionMessage {
		if(!topicoRepository.findById(id).isPresent()){
			throw new ExceptionMessage();
		}
		return new DetalhesDoTopicoDto(topicoRepository.getOne(id));
	}

	@PutMapping("/{id}")
	@Transactional
    @CacheEvict(value = LISTA_TOPICOS,allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm topicoForm) throws ExceptionMessage {
		Topico topico = topicoForm.atualizer(id,topicoRepository);

		return ResponseEntity.ok(new TopicoDto(topico));
	}

	@DeleteMapping("/{id}")
    @CacheEvict(value = (LISTA_TOPICOS),allEntries = true)
	public ResponseEntity deletar(@PathVariable Long id) throws ExceptionMessage {
		if(!topicoRepository.findById(id).isPresent())
			throw new ExceptionMessage();
		topicoRepository.deleteById(id);
		return ResponseEntity.ok().build();

	}

}
