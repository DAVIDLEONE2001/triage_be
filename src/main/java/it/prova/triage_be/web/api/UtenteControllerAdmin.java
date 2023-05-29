package it.prova.triage_be.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.triage_be.dto.UtenteDTO;
import it.prova.triage_be.dto.UtenteDTOPut;
import it.prova.triage_be.dto.UtenteDTOShow;
import it.prova.triage_be.model.Utente;
import it.prova.triage_be.service.UtenteService;
import it.prova.triage_be.web.api.exception.IdNotNullForInsertException;
import it.prova.triage_be.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("/api/admin")
public class UtenteControllerAdmin {

	@Autowired
	private UtenteService utenteService;

	@GetMapping
	public List<UtenteDTOShow> getAll() {
		// senza DTO qui hibernate dava il problema del N + 1 SELECT
		// (probabilmente dovuto alle librerie che serializzano in JSON)
		return UtenteDTOShow.createUtenteDTOShowListFromModelList(utenteService.listAllUtenti(), true);
	}

	@GetMapping("/{id}")
	public UtenteDTOShow findById(@PathVariable(value = "id", required = true) long id) {
		Utente utente = utenteService.caricaSingoloUtenteConRuoli(id);

		if (utente == null)
			throw new UtenteNotFoundException("Tavolo not found con id: " + id);

		return UtenteDTOShow.buildUtenteDTOShowFromModel(utente, true);
	}
//
//	// gli errori di validazione vengono mostrati con 400 Bad Request ma
//	// elencandoli grazie al ControllerAdvice
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UtenteDTOShow createNew(@Valid @RequestBody UtenteDTO utenteInput) {
//		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
//		// non sta bene
		if (utenteInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		;
		Utente tavoloAggiornato = utenteService.inserisciNuovo(utenteInput.buildUtenteModel(true));
		return UtenteDTOShow
				.buildUtenteDTOShowFromModel(utenteService.caricaSingoloUtenteConRuoli(tavoloAggiornato.getId()), true);
	}

	@PutMapping("/{id}")
	public UtenteDTOShow update(@Valid @RequestBody UtenteDTOPut utenteInput,
			@PathVariable(required = true) Long id) {
		Utente utente = utenteService.caricaSingoloUtente(id);

		if (utente == null)
			throw new UtenteNotFoundException("Tavolo not found con id: " + id);

		utenteInput.setId(id);
		Utente utenteAggiornato = utenteService.aggiornaPut(utenteInput.buildUtenteModel(true));
		return UtenteDTOShow.buildUtenteDTOShowFromModel(utenteAggiornato, true);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		utenteService.rimuovi(id);
	}

	@PostMapping("/search")
	public ResponseEntity<Page<UtenteDTOShow>> searchPaginated(@RequestBody UtenteDTO example,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Page<Utente> entityPageResults = utenteService.findByExample(example.buildUtenteModel(true),
				pageNo, pageSize, sortBy);
		return new ResponseEntity<Page<UtenteDTOShow>>(UtenteDTOShow.fromModelPageToDTOPage(entityPageResults),
				HttpStatus.OK);} 
	
	
}
