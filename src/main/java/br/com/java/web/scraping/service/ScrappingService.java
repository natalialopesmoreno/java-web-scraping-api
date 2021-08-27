package br.com.java.web.scraping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.java.web.scraping.dto.PartidaGoogleDTO;
import br.com.java.web.scraping.entity.Partida;
import br.com.java.web.scraping.util.ScrapingUtil;

@Service
public class ScrappingService {
	
	@Autowired
	private ScrapingUtil scrapingUtil;
	
	@Autowired
	private PartidaService partidaService;
	
	public void verificaPartidasPeriodo() {
		Integer quantidadePartidas = partidaService.buscarQuantidadePartidasPeriod();
		
		if(quantidadePartidas > 0) {
			List<Partida> partidas = partidaService.listarPartidasPeriodo();
			
			partidas.forEach(partida -> {
				String urlPartida = scrapingUtil.montaUrlGoogle(
						partida.getEquipeCasa().getNomeEquipe(),
						partida.getEquipeVisitante().getNomeEquipe()

						);
				PartidaGoogleDTO partidaGoogle = scrapingUtil.obtemInfoPartida(urlPartida);
				
				partidaService.atualizarPartida(partida , partidaGoogle);
			});
		}
	}

}
