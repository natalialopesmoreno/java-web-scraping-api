package br.com.java.web.scraping.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.java.web.scraping.service.ScrappingService;
import br.com.java.web.scraping.util.DataUtil;


@Configuration
@EnableScheduling
public class PartidaTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartidaTask.class);
	private static final String TIME_ZONE = "America/Sao_Paulo";
	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
	
	@Autowired
	private ScrappingService scrapingService;
	
	@Scheduled(cron = "0/30 * 19-23 * * WED", zone= TIME_ZONE)
	public void taskPartidaQuartaFeira() {
		inicializaAgendamento(" taskPartidaQuartaFeira()");
	}
	
	@Scheduled(cron = "0/30 * 19-23 * * THU", zone= TIME_ZONE)
	public void taskPartidaQuintaFeira() {
		inicializaAgendamento(" taskPartidaQuintaFeira()");
	}
	
	@Scheduled(cron = "0/30 * 16-23 * * SAT", zone= TIME_ZONE)
	public void taskPartidaSabado() {
		inicializaAgendamento(" taskPartidaSabado()");
	}
	
	@Scheduled(cron = "0/30 * 16-23 * * SUN", zone= TIME_ZONE)
	public void taskPartidaDomingoTarde() {
		inicializaAgendamento(" taskPartidaDomingoTarde()");
	}
	
	@Scheduled(cron = "0/30 * 11-13 * * SUN", zone= TIME_ZONE)
	public void taskPartidaDomingoManha() {
		inicializaAgendamento(" taskPartidaDomingoManha()");
	}
	
	
	
	private void inicializaAgendamento(String diaSemana) {
		this.gravaLogInfo(String.format("%s:%s", diaSemana, DataUtil.formatarDateEmString(new Date(), DD_MM_YYYY_HH_MM_SS)));
		scrapingService.verificaPartidasPeriodo();
	}
	
	private void gravaLogInfo(String mensagem) {
		LOGGER.info(mensagem);
	}
	
	
	
}
