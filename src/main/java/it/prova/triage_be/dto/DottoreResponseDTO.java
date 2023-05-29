package it.prova.triage_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DottoreResponseDTO {

	private String codiceDottore;
	private String nome;
	private String cognome;
	private Boolean inVisita;
	private Boolean inServizio;


}
