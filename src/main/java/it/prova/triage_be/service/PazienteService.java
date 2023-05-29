package it.prova.triage_be.service;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.triage_be.model.Paziente;

public interface PazienteService {

	public List<Paziente> listAllPazienti();

	public Paziente caricaSingoloPaziente(Long id);

	public Paziente aggiorna(Paziente pazienteInstance);

	public Paziente inserisciNuovo(Paziente pazienteInstance);

	public void rimuovi(Long idToRemove);
	
	Page<Paziente> findByExample(Paziente example, Integer pageNo, Integer pageSize, String sortBy);
	
	Paziente findByCodiceFiscale(String codiceFiscale); 
}
