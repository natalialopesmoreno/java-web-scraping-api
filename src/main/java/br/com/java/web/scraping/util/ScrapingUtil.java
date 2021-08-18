package br.com.java.web.scraping.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.java.web.scraping.dto.PartidaGoogleDTO;

public class ScrapingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	private static final String BASE_URL_GOOGLE = "https://www.google.com.br/search?q=";
	
	private static final String CASA = "casa";
	private static final String VISITANTE = "visitante";


	public static void main(String[] args) {

		String url = BASE_URL_GOOGLE + "corinthians+x+palmeiras+08/08/2020";

		ScrapingUtil scraping = new ScrapingUtil();
		scraping.obtemInfoPartida(url);
	}

	public PartidaGoogleDTO obtemInfoPartida(String url) {
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			String title = document.title();
			LOGGER.info("Título da página : {}", title);

			StatusPartida statusPartida = obtemStatusPartida(document);
			LOGGER.info("Status partida : {}", statusPartida.toString());

			if( statusPartida != StatusPartida.PARTIDA_NAO_INICIADA){
			
			LOGGER.info("Tempo partida : {}", obtemTempoPartida(document));
			
			Integer placarEquipeCasa = recuperarPlacarEquipeCasa(document);
			LOGGER.info("Placar da equipe da casa :" + placarEquipeCasa);
			
			String goalsEquipeCasa = recuperarGoalsEquipeCasa(document);
			LOGGER.info("Gols da equipe da casa : " + goalsEquipeCasa);
			
			Integer placarEquipeVisitante = recuperarPlacarEquipeVisitante(document);
			LOGGER.info("Placar da equipe visitante :" + placarEquipeVisitante);
			
			String goalsEquipeVisitante = recuperarGoalsEquipeVisitante(document);
			LOGGER.info("Gols da equipe visitante : " + goalsEquipeVisitante);
			
			Integer placarEstendidoEquipeCasa = buscaPenalidades(document, CASA);
			LOGGER.info("Placar estendido equipe da casa :" + placarEstendidoEquipeCasa);
			
			Integer placarEstendidoEquipeVisitante = buscaPenalidades(document, VISITANTE);
			LOGGER.info("Placar estendido visitante :" + placarEstendidoEquipeVisitante);

			
			
			};
			
			String nomeEquipeCasa = recuperaNomeEquipeCasa(document);
			LOGGER.info("Nome equipe da casa: {}", nomeEquipeCasa);
			
			String urlLogoEquipeCasa = recuperaLogoEquipeCasa(document);
			LOGGER.info("Url logo equipe da casa: {}", urlLogoEquipeCasa);

			
			String nomeEquipeVisitante = recuperaNomeEquipeVisitante(document);
			LOGGER.info("Nome equipe visitante: {}", nomeEquipeVisitante);
			
			String urlLogoEquipeVisitante = recuperaLogoEquipeVisitante(document);
			LOGGER.info("Url logo equipe visitante: {}", urlLogoEquipeVisitante);

		} catch (IOException e) {
			LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
			e.printStackTrace();
		}

		return partida;
	}



	public Integer buscaPenalidades(Document document, String tipoEquipe) {

		boolean isPenalidades = document.select("div[class=imso_mh_s__psn-sc]").isEmpty();
		if(!isPenalidades) {
			String penalidades = document.select("div[class=imso_mh_s__psn-sc]").text();
			String penalidadesCompleta = penalidades.substring(0,5).replace(" ", "");
			String [] divisao = penalidadesCompleta.split("-");
			LOGGER.info("penalidades: " + penalidades);
			
			return tipoEquipe.equals(CASA) ? formataPlacarStringInteger(divisao[0]) : formataPlacarStringInteger(divisao[1]);
		}
		
		return null;
	}
	
	public Integer formataPlacarStringInteger(String placar) {
		Integer valor;
		try {
			valor = Integer.parseInt(placar);
		}catch(Exception e){
			valor = 0;
		}
		return valor;
	}

	public String recuperarGoalsEquipeVisitante(Document document) {
		List<String> goalsEquipe = new ArrayList<String>();
		Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__right-team]").select("div[class=imso_gs__gs-r]");
		
		elementos.forEach(e ->{
			String infoGol = e.select("div[class=imso_gs__gs-r]").text();
			goalsEquipe.add(infoGol);
		}); 
		
		return String.join(", ", goalsEquipe);
	}

	public String recuperarGoalsEquipeCasa(Document document) {
		
		List<String> goalsEquipe = new ArrayList<String>();
		Elements elementos = document.select("div[class=imso_gs__tgs imso_gs__left-team]").select("div[class=imso_gs__gs-r]");
		
		for(Element e : elementos) {
			String infoGol = e.select("div[class=imso_gs__gs-r]").text();
			goalsEquipe.add(infoGol);
		}
		
		return String.join(", ", goalsEquipe);
	}

	public Integer recuperarPlacarEquipeVisitante(Document document) {
		String placarDaCasa = document.select("div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]").text();
		return formataPlacarStringInteger(placarDaCasa);
	}

	public Integer recuperarPlacarEquipeCasa(Document document) {
		
		String placarDaCasa = document.select("div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]").text();
		return formataPlacarStringInteger(placarDaCasa);
	}

	public String recuperaNomeEquipeCasa(Document document) {
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = elemento.select("span").text();
		
		return nomeEquipe;
		}
	
	public String recuperaLogoEquipeVisitante(Document document) {
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo =  elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		
		return urlLogo;
	}

	public String recuperaNomeEquipeVisitante(Document document) {

		Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = elemento.select("span").text();
		
		return nomeEquipe;
	}
	
	public String recuperaLogoEquipeCasa(Document document) {
		Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo =  elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		
		return urlLogo;
	}

	public StatusPartida obtemStatusPartida(Document document) {

		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;

		boolean isTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		if (!isTempoPartida) {
			String tempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;

			if (tempoPartida.contains("Pênaltis")) {
				statusPartida = StatusPartida.PARTIDA_PENALTIS;
			}
		}

		isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();

		if (!isTempoPartida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
		return statusPartida;
	}

	public String obtemTempoPartida(Document document) {
		String tempoDePartida = null;
		boolean isTempoPartida = document.select("div[class=imso_mh__lv-m-stts-cont]").isEmpty();
		if (!isTempoPartida) {
			tempoDePartida = document.select("div[class=imso_mh__lv-m-stts-cont]").first().text();
		}

		isTempoPartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").isEmpty();
		if (!isTempoPartida) {
			tempoDePartida = document.select("span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]").first()
					.text();
		}
		return corrigeTempoPartida(tempoDePartida);
	}

	public String corrigeTempoPartida(String tempo) {
		if (tempo.contains("'")) {
			return tempo.replace("'", " min");
		}  else {
			return tempo;
		}
	}
}
