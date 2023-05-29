package it.prova.triage_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DottoreResponseDTO {

	private String codiceDottore;
	private String nome;
	private String cognome;
	private Boolean inVisita;
	private Boolean inServizio;

	public boolean isNotValid() {

		return this.codiceDottore == null || this.nome == null || this.cognome == null 
				|| this.inServizio == null; 
			
		

		

	}

}
