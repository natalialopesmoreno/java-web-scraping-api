package br.com.java.web.scraping.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String nomeEquipe;
	@NotBlank
	private String urlLogoEquipe;

}
