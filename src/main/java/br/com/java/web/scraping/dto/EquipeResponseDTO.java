package br.com.java.web.scraping.dto;

import java.io.Serializable;
import java.util.List;

import br.com.java.web.scraping.entity.Equipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipeResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Equipe> equipes;

}
