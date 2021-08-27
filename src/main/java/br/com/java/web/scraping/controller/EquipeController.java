package br.com.java.web.scraping.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.java.web.scraping.dto.EquipeDTO;
import br.com.java.web.scraping.dto.EquipeResponseDTO;
import br.com.java.web.scraping.entity.Equipe;
import br.com.java.web.scraping.exception.StandardError;
import br.com.java.web.scraping.service.EquipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("API de equipes")
@RestController
@RequestMapping("api/v1/equipes")
public class EquipeController {

	@Autowired
	private EquipeService equipeService;
	
	@ApiOperation(value = "Buscar equipe por id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Equipe.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class),

	})
	@GetMapping("/{id}")
	public ResponseEntity<Equipe> buscarEquipeId(@PathVariable ("id") Long id){
	
		return ResponseEntity.ok().body(equipeService.buscarEquipeId(id));
	}
	
	@ApiOperation(value = "Listar Equipes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = EquipeResponseDTO.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class),

	})
	@GetMapping()
	public ResponseEntity<EquipeResponseDTO> listarEquipes(){
	
		return ResponseEntity.ok().body(equipeService.listarEquipes());
	}
	
	
	@ApiOperation(value = "Inserir de Equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Equipe.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class),

	})
	@PostMapping()
	public ResponseEntity<Equipe> inserirEquipe(@Valid @RequestBody EquipeDTO dto){
		Equipe equipe = equipeService.inserirEquipe(dto);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(equipe.getId()).toUri();
		return ResponseEntity.created(location).body(equipe);
	}
	
	@ApiOperation(value = "Alterar de Equipe")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No content", response = Equipe.class),
			@ApiResponse(code = 400, message = "Bad Request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = StandardError.class),

	})
	@PutMapping("/{id}")
	public ResponseEntity<Void> alterarEquipe(@PathVariable Long id, @Valid @RequestBody EquipeDTO dto){
		equipeService.alterarEquipe(id, dto);
		return ResponseEntity.noContent().build();
	}
}
