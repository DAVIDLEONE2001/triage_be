package it.prova.triage_be.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import it.prova.triage_be.model.Ruolo;
import it.prova.triage_be.model.StatoUtente;
import it.prova.triage_be.model.Utente;

public class UtenteDTOShow {

	private String nome;
	private String cognome;
	private String type = "Bearer";
	private String username;
	private List<String> roles;
	private String stato;
	private LocalDate dataCreazione;

	public UtenteDTOShow(String nome, String cognome, String username, String stato, LocalDate dataCreazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.stato = stato;
		this.dataCreazione = dataCreazione;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public static UtenteDTOShow buildUtenteDTOShowFromModel(Utente utenteModel, boolean includeRuoli) {
		UtenteDTOShow result = new UtenteDTOShow(utenteModel.getNome(), utenteModel.getCognome(),
				utenteModel.getUsername(), utenteModel.getStato().toString(), utenteModel.getDataRegistrazione());

		if (includeRuoli)
			result.setRoles(
					utenteModel.getRuoli().stream().map(roleItem -> roleItem.getCodice()).collect(Collectors.toList()));
		return result;
	}

	public static List<UtenteDTOShow> createUtenteDTOShowListFromModelList(List<Utente> modelListInput,
			boolean includeRuoli) {
		return modelListInput.stream().map(utenteEntity -> {
			UtenteDTOShow result = UtenteDTOShow.buildUtenteDTOShowFromModel(utenteEntity, includeRuoli);
			if (includeRuoli)
				result.setRoles(utenteEntity.getRuoli().stream().map(roleItem -> roleItem.getCodice())
						.collect(Collectors.toList()));
			return result;
		}).collect(Collectors.toList());
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public static Page<UtenteDTOShow> fromModelPageToDTOPage(Page<Utente> input) {
		return new PageImpl<>(createUtenteDTOShowListFromModelList(input.getContent(), true),
				PageRequest.of(input.getPageable().getPageNumber(), input.getPageable().getPageSize(),
						input.getPageable().getSort()),
				input.getTotalElements());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

//	public Utente buildUtenteModel(boolean includeIdRoles) {
////		Utente result = new Utente( this.nome, this.cognome, this.username,
////				this.dataCreazione, this.stato);
//		Utente result = Utente.builder().nome(this.nome).cognome(this.cognome)
//				.username(this.username).dataRegistrazione(this.dataCreazione).stato(StatoUtente.valueOf(this.stato)).build();
//		if (includeIdRoles && roles != null)
//			result.setRuoli(Arrays.asList(roles).stream().map(codice -> new Ruolo(codice)).collect(Collectors.toSet()));
//
//		return result;
//	}
}