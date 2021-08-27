package br.com.java.web.scraping.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartidaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String nomeEquipeCasa;
	
	@NotBlank
	private String nomeEquipeVisitante;
	@NotBlank
	private String localPartida;
	
	@NotNull
	@ApiModelProperty(example = "dd/MM/yyyy HH:mm")
	@JsonFormat(shape = Shape.STRING, pattern= "dd/MM/yyyy HH:mm", timezone="America/Sao_Paulo")
	private Date dataHoraPartida;

}
