package it.prova.triage_be.service;

import java.util.List;

import it.prova.triage_be.model.Ruolo;

public interface RuoloService {
	public List<Ruolo> listAll();

	public Ruolo caricaSingoloElemento(Long id);

	public void aggiorna(Ruolo ruoloInstance);

	public void inserisciNuovo(Ruolo ruoloInstance);

	public void rimuovi(Long idToRemove);

	public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice);

	Ruolo cercaPerCodice(String codice);

}
