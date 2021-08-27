package br.com.java.web.scraping.service;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.java.web.scraping.dto.PartidaDTO;
import br.com.java.web.scraping.dto.PartidaGoogleDTO;
import br.com.java.web.scraping.dto.PartidaResponseDTO;
import br.com.java.web.scraping.entity.Partida;
import br.com.java.web.scraping.exception.NotFoundException;
import br.com.java.web.scraping.repository.PartidaRepository;

@Service
public class PartidaService {
	
	@Autowired
	private PartidaRepository partidaRepository;
	
	@Autowired 
	private ModelMapper model;
	
	@Autowired
	private EquipeService equipeService;

	public Partida buscarPartidaId(Long id) {
		return partidaRepository.findById(id).orElseThrow(()-> new NotFoundException("Nenhuma partida encontrada com o id informado: " + id));

	}

	public PartidaResponseDTO listarPartidas() {
		PartidaResponseDTO partidas = new PartidaResponseDTO();
		partidas.setPartidas(partidaRepository.findAll());
		return partidas;
	}

	public Partida inserirPartida(@Valid PartidaDTO dto) {
		Partida partida = model.map(dto, Partida.class);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		return salvarPartida(partida) ;
	}
	
	public Partida salvarPartida(Partida partida) {
		return partidaRepository.save(partida);
	}

	public void alterarPartida(Long id,  PartidaDTO dto) {
		boolean existe = partidaRepository.existsById(id);
		
		if(!existe) {
			throw new NotFoundException("Não foi poss~ivel alterar a partida: ID inexistente");
		}
		
		Partida partida = buscarPartidaId(id);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		partida.setDataHoraPartida(dto.getDataHoraPartida());
		partida.setLocalPartida(dto.getLocalPartida());
		salvarPartida(partida);
	}

	public void atualizarPartida(Partida partida, PartidaGoogleDTO partidaGoogle) {
		partida.setPlacarEquipeCasa(partidaGoogle.getPlacarEquipeCasa());
		partida.setPlacarEquipeVisitante(partidaGoogle.getPlacarEquipeVisitante());
		partida.setGolsEquipeCasa(partidaGoogle.getGolsEquipeCasa());
		partida.setGolsEquipeVisitante(partidaGoogle.getGolsEquipeVisitante());
		partida.setPlacarEstendidoEquipeCasa(partidaGoogle.getPlacarEstendidoEquipeCasa());
		partida.setPlacarEstendidoEquipeVisitante(partidaGoogle.getPlacarEstendidoEquipeVisitante());
		partida.setTempoPartida(partidaGoogle.getTempoPartida());
		
		

	}

	public List<Partida> listarPartidasPeriodo() {
		return partidaRepository.listarPartidasPeriodo();
	}

	public Integer buscarQuantidadePartidasPeriod() {
		return partidaRepository.buscarQuantidadePartidasPeriodo();
	}

}
