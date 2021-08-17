package br.com.java.web.scraping.util;

import java.io.IOException;

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

	public static void main(String[] args) {

		String url = BASE_URL_GOOGLE + "brasil+x+bolivia+09/10/2020";

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
			LOGGER.info("Quantidade de gols da equipe da casa :" + placarEquipeCasa);
			
			Integer placarEquipeVisitante = recuperarPlacarEquipeVisitante(document);
			LOGGER.info("Quantidade de gols da equipe visitante :" + placarEquipeVisitante);
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



	private Integer recuperarPlacarEquipeVisitante(Document document) {
		String placarDaCasa = document.select("div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]").text();
		return Integer.valueOf(placarDaCasa);
	}

	private Integer recuperarPlacarEquipeCasa(Document document) {
		
		String placarDaCasa = document.select("div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]").text();
		return Integer.valueOf(placarDaCasa);
	}

	private String recuperaNomeEquipeCasa(Document document) {
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = elemento.select("span").text();
		
		return nomeEquipe;
		}
	
	private String recuperaLogoEquipeVisitante(Document document) {
		Element elemento = document.selectFirst("div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]");
		String urlLogo =  elemento.select("img[class=imso_btl__mh-logo]").attr("src");
		
		return urlLogo;
	}

	private String recuperaNomeEquipeVisitante(Document document) {

		Element elemento = document.selectFirst("div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]");
		String nomeEquipe = elemento.select("span").text();
		
		return nomeEquipe;
	}
	
	private String recuperaLogoEquipeCasa(Document document) {
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
