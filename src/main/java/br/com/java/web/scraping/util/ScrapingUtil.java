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
import org.springframework.stereotype.Service;

import br.com.java.web.scraping.dto.PartidaGoogleDTO;

@Service
public class ScrapingUtil {
	

	private static final String DIV_PARTIDA_ENCERRADA = "span[class=imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]";
	private static final String DIV_PARTIDA_ANDAMENTO = "div[class=imso_mh__lv-m-stts-cont]";
	private static final String DIV_DADOS_EQUIPE_CASA = "div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]";
	private static final String DIV_DADOS_EQUIPE_VISITANTE = "div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]";
	
	private static final String ITEM_LOGO = "img[class=imso_btl__mh-logo]";
	
	private static final String DIV_PLACAR_EQUIPE_VISITANTE = "div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]";
	private static final String DIV_PLACAR_EQUIPE_CASA = "div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]";
	
	private static final String DIV_GOLS_EQUIPE_VISITANTE = "div[class=imso_gs__tgs imso_gs__right-team]";
	private static final String DIV_GOLS_EQUIPE_CASA = "div[class=imso_gs__tgs imso_gs__left-team]";
	private static final String ITEM_GOL = "div[class=imso_gs__gs-r]";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);
	private static final String BASE_URL_GOOGLE = "https://www.google.com.br/search?q=";
	
	private static final String DIV_PENALIDADES = "div[class=imso_mh_s__psn-sc]";
	private static final String CASA = "casa";
	private static final String VISITANTE = "visitante";


//	public static void main(String[] args) {
//
//
//		ScrapingUtil scraping = new ScrapingUtil();
//		
//		String url = scraping.montaUrlGoogle("Palmeiras", "São Paulo", " ");
//		scraping.obtemInfoPartida(url);
//	}

	public PartidaGoogleDTO obtemInfoPartida(String url) {
		PartidaGoogleDTO partida = new PartidaGoogleDTO();
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			String title = document.title();
			LOGGER.info("Título da página : {}", title);

			StatusPartida statusPartida = obtemStatusPartida(document);
			partida.setStatusPartida(statusPartida);
			LOGGER.info("Status partida : {}", statusPartida.toString());

			if( statusPartida != StatusPartida.PARTIDA_NAO_INICIADA){
			String tempoPartida = obtemTempoPartida(document);
			partida.setTempoPartida(tempoPartida);
			LOGGER.info("Tempo partida : {}", tempoPartida);
			
			Integer placarEquipeCasa = recuperarPlacarEquipe(document, DIV_PLACAR_EQUIPE_CASA);
			partida.setPlacarEquipeCasa(placarEquipeCasa);
			LOGGER.info("Placar da equipe da casa :" + placarEquipeCasa);
			
			String goalsEquipeCasa = recuperarGoalsEquipe(document, DIV_GOLS_EQUIPE_CASA);
			partida.setGolsEquipeCasa(goalsEquipeCasa);
			LOGGER.info("Gols da equipe da casa : " + goalsEquipeCasa);
			
			Integer placarEquipeVisitante = recuperarPlacarEquipe(document, DIV_PLACAR_EQUIPE_VISITANTE);
			partida.setPlacarEquipeVisitante(placarEquipeVisitante);
			LOGGER.info("Placar da equipe visitante :" + placarEquipeVisitante);
			
			String goalsEquipeVisitante = recuperarGoalsEquipe(document, DIV_GOLS_EQUIPE_VISITANTE);
			partida.setGolsEquipeVisitante(goalsEquipeVisitante);
			LOGGER.info("Gols da equipe visitante : " + goalsEquipeVisitante);
			
			Integer placarEstendidoEquipeCasa = buscaPenalidades(document, CASA);
			partida.setPlacarEstendidoEquipeCasa(placarEstendidoEquipeCasa);
			LOGGER.info("Placar estendido equipe da casa :" + placarEstendidoEquipeCasa);
			
			Integer placarEstendidoEquipeVisitante = buscaPenalidades(document, VISITANTE);
			partida.setPlacarEstendidoEquipeVisitante(placarEstendidoEquipeVisitante);
			LOGGER.info("Placar estendido visitante :" + placarEstendidoEquipeVisitante);

			
			
			};
			
			String nomeEquipeCasa = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_CASA);
			partida.setNomeEquipeCasa(nomeEquipeCasa);
			LOGGER.info("Nome equipe da casa: {}", nomeEquipeCasa);
			
			String urlLogoEquipeCasa = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_CASA);
			partida.setUrlLogoEquipeCasa(urlLogoEquipeCasa);
			LOGGER.info("Url logo equipe da casa: {}", urlLogoEquipeCasa);
			
			String nomeEquipeVisitante = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_VISITANTE);
			partida.setNomeEquipeVisitante(nomeEquipeVisitante);
			LOGGER.info("Nome equipe visitante: {}", nomeEquipeVisitante);
			
			String urlLogoEquipeVisitante = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_VISITANTE);
			partida.setUrlLogoEquipeVisitante(urlLogoEquipeVisitante);
			LOGGER.info("Url logo equipe visitante: {}", urlLogoEquipeVisitante);

		} catch (IOException e) {
			LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
			e.printStackTrace();
		}

		return partida;
	}



	public Integer buscaPenalidades(Document document, String tipoEquipe) {

		boolean isPenalidades = document.select(DIV_PENALIDADES).isEmpty();
		if(!isPenalidades) {
			String penalidades = document.select(DIV_PENALIDADES).text();
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

	public String recuperarGoalsEquipe(Document document, String divGols) {
		List<String> goalsEquipe = new ArrayList<String>();
		Elements elementos = document.select(divGols).select(ITEM_GOL);
		
		elementos.forEach(e ->{
			String infoGol = e.select(ITEM_GOL).text();
			goalsEquipe.add(infoGol);
		}); 
		
		return String.join(", ", goalsEquipe);
	}

	public Integer recuperarPlacarEquipe(Document document, String itemHtml) {
		
		String placarDaCasa = document.select(itemHtml).text();
		return formataPlacarStringInteger(placarDaCasa);
	}

	public String recuperaNomeEquipe(Document document, String itemHtml ) {
		Element elemento = document.selectFirst(itemHtml);
		String nomeEquipe = elemento.select("span").text();
		
		return nomeEquipe;
		}
	
	
	public String recuperaLogoEquipe(Document document, String itemHtml) {
		Element elemento = document.selectFirst(itemHtml);
		String urlLogo =  elemento.select(ITEM_LOGO).attr("src");
		
		return urlLogo;
	}
	

	public StatusPartida obtemStatusPartida(Document document) {

		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;

		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		if (!isTempoPartida) {
			String tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;

			if (tempoPartida.contains("Pênaltis")) {
				statusPartida = StatusPartida.PARTIDA_PENALTIS;
			}
		}

		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();

		if (!isTempoPartida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
		return statusPartida;
	}

	public String obtemTempoPartida(Document document) {
		String tempoDePartida = null;
		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		if (!isTempoPartida) {
			tempoDePartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
		}

		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		if (!isTempoPartida) {
			tempoDePartida = document.select(DIV_PARTIDA_ENCERRADA).first()
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
	
	public String montaUrlGoogle(String nomeEquipeCasa , String nomeEquipeVisitante, String dataJogo) {
		try {
			String equipeCasa = nomeEquipeCasa.replace(" ", "+").replace("-", "+").toLowerCase();
			String equipeVisitante = nomeEquipeVisitante.replace(" ", "+").replace("-", "+").toLowerCase();
			String data = dataJogo.replace(" ", "").replace("-", "/");
			return BASE_URL_GOOGLE + equipeCasa + "x" + equipeVisitante + data;
		}catch(Exception e) {
			LOGGER.info("ERRO: " + e.getMessage());
		}
		
		return null;
	}
	
	public String montaUrlGoogle(String nomeEquipeCasa , String nomeEquipeVisitante) {
		try {
			String equipeCasa = nomeEquipeCasa.replace(" ", "+").replace("-", "+").toLowerCase();
			String equipeVisitante = nomeEquipeVisitante.replace(" ", "+").replace("-", "+").toLowerCase();
			return BASE_URL_GOOGLE + equipeCasa + "x" + equipeVisitante;
		}catch(Exception e) {
			LOGGER.info("ERRO: " + e.getMessage());
		}
		
		return null;
	}
}
