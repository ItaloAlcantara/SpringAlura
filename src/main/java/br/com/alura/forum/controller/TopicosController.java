package br.com.alura.forum.controller;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;

import br.com.alura.forum.config.ExceptionMessage;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
	@GetMapping
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @RequestParam int pag, @RequestParam int qtd, @RequestParam String ordenacao) {

		PageRequest paginacao = PageRequest.of(pag,qtd,Sort.Direction.ASC,ordenacao);

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso,paginacao);
			return TopicoDto.converter(topicos);
		}
	}

	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){

		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri= uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) throws ExceptionMessage {
		if(!topicoRepository.findById(id).isPresent()){
			throw new ExceptionMessage();
		}
		return new DetalhesDoTopicoDto(topicoRepository.getOne(id));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm topicoForm) throws ExceptionMessage {
		Topico topico = topicoForm.atualizer(id,topicoRepository);

		return ResponseEntity.ok(new TopicoDto(topico));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deletar(@PathVariable Long id) throws ExceptionMessage {
		if(!topicoRepository.findById(id).isPresent())
			throw new ExceptionMessage();
		topicoRepository.deleteById(id);
		return ResponseEntity.ok().build();

	}

}
