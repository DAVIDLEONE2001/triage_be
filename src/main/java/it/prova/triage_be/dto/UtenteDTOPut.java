package it.prova.triage_be.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import it.prova.triage_be.model.Ruolo;
import it.prova.triage_be.model.StatoUtente;
import it.prova.triage_be.model.Utente;

public class UtenteDTOPut {

	private Long id;

	private String username;

	private String nome;

	private String cognome;

	private LocalDate dateCreated;

	private StatoUtente stato;

	private Long[] ruoliIds;

	public UtenteDTOPut() {
	}

	public UtenteDTOPut(Long id, String username, String nome, String cognome, StatoUtente stato) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.stato = stato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}



	public Long[] getRuoliIds() {
		return ruoliIds;
	}

	public void setRuoliIds(Long[] ruoliIds) {
		this.ruoliIds = ruoliIds;
	}

	public Utente buildUtenteModel(boolean includeIdRoles) {
//		Utente result = new Utente(this.id, this.username, this.password, this.nome, this.cognome, this.email,
//				this.dateCreated, this.stato);
		Utente result = Utente.builder().id(this.id).username(this.username).nome(this.nome)
				.cognome(this.cognome).dataRegistrazione(dateCreated).stato(this.stato).build();
		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));

		return result;
	}

	// niente password...
	public static UtenteDTOPut buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTOPut result = new UtenteDTOPut(utenteModel.getId(), utenteModel.getUsername(), utenteModel.getNome(),
				utenteModel.getCognome(), utenteModel.getStato());

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

}
