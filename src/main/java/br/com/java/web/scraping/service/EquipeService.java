package br.com.java.web.scraping.service;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.java.web.scraping.dto.EquipeDTO;
import br.com.java.web.scraping.dto.EquipeResponseDTO;
import br.com.java.web.scraping.entity.Equipe;
import br.com.java.web.scraping.exception.BadRequestException;
import br.com.java.web.scraping.exception.NotFoundException;
import br.com.java.web.scraping.repository.EquipeRepository;

@Service
public class EquipeService {

	
	@Autowired
	private EquipeRepository equipeRepository;
	
	//Esse ModelMapper me permite converter objetos posso converter automaticamente um dto para uma entity
	@Autowired 
	private ModelMapper model;
	
	public Equipe buscarEquipeId(Long id) {
		return equipeRepository.findById(id).orElseThrow(()-> new NotFoundException("Nenhuma equipe encontrada com o id informado: " + id));
	}
	
	public Equipe buscarEquipePorNome(String nome) {
		
		return equipeRepository.findByNomeEquipe(nome)
				.orElseThrow(() -> new NotFoundException("Nenhuma equipe encontrada com o nome informado: " + nome));
	}

	public EquipeResponseDTO listarEquipes() {
		EquipeResponseDTO equipes = new EquipeResponseDTO();
		equipes.setEquipes(equipeRepository.findAll());
		
		return equipes;
	}

	public Equipe inserirEquipe( EquipeDTO dto) {
		boolean exists = equipeRepository.existsByNomeEquipe(dto.getNomeEquipe());
		
		if(exists) {
			throw new BadRequestException("Jã existe uma queipe cadastrada com o nome informado");
		}
		
		Equipe equipe = model.map(dto, Equipe.class);
		return equipeRepository.save(equipe);
	}

	public void alterarEquipe(Long id,  EquipeDTO dto) {
		boolean exists = equipeRepository.existsById(id);
		if(!exists) {
			throw new BadRequestException("Não foi possível alterar a equipe: ID inexistente");
		}
		
		Equipe equipe = model.map(dto, Equipe.class);
		equipe.setId(id);
		equipeRepository.save(equipe);
	}

}
