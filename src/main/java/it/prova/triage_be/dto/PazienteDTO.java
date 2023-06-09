package it.prova.triage_be.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.model.Ruolo;
import it.prova.triage_be.model.StatoPaziente;
import it.prova.triage_be.model.Utente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PazienteDTO {

	private Long id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private LocalDate dataRegistrazione;
	private StatoPaziente stato;
	private String codiceDottore;

	public static PazienteDTO buildPazienteDTOFromModel(Paziente pazienteModel) {

		PazienteDTO result = PazienteDTO.builder().id(pazienteModel.getId()).nome(pazienteModel.getNome())
				.cognome(pazienteModel.getCognome()).codiceFiscale(pazienteModel.getCodiceFiscale())
				.dataRegistrazione(pazienteModel.getDataRegistrazione()).stato(pazienteModel.getStato())
				.codiceDottore(pazienteModel.getCodiceDottore()).build();
		return result;
	}

	public static List<PazienteDTO> createPazienteDTOListFromModelList(List<Paziente> modelListInput) {
		return modelListInput.stream().map(utenteEntity -> {
			PazienteDTO result = PazienteDTO.buildPazienteDTOFromModel(utenteEntity);
			return result;
		}).collect(Collectors.toList());
	}

	public Paziente buildPazienteModel() {
		Paziente result = Paziente.builder().id(this.id).nome(this.nome).cognome(this.cognome)
				.dataRegistrazione(this.dataRegistrazione).stato(this.stato).codiceDottore(this.codiceDottore)
				.codiceFiscale(this.codiceFiscale).codiceDottore(this.codiceDottore).build();

		return result;
	}
	
	public static Page<PazienteDTO> fromModelPageToDTOPage(Page<Paziente> input) {
		return new PageImpl<>(createPazienteDTOListFromModelList(input.getContent()),
				PageRequest.of(input.getPageable().getPageNumber(), input.getPageable().getPageSize(),
						input.getPageable().getSort()),
				input.getTotalElements());
	}
}
