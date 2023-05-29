package it.prova.triage_be.web.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage_be.dto.PazienteDTO;
import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.service.PazienteService;
import it.prova.triage_be.web.api.exception.IdNotNullForInsertException;
import it.prova.triage_be.web.api.exception.PazienteNotFoundException;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {

	@Autowired
	private PazienteService pazienteService;
	


	@GetMapping
	public List<PazienteDTO> getAll() {
		// senza DTO qui hibernate dava il problema del N + 1 SELECT
		// (probabilmente dovuto alle librerie che serializzano in JSON)
		return PazienteDTO.createPazienteDTOListFromModelList(pazienteService.listAllPazienti());
	}

	@GetMapping("/{id}")
	public PazienteDTO findById(@PathVariable(value = "id", required = true) long id) {
		Paziente paziente = pazienteService.caricaSingoloPaziente(id);

		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + id);

		return PazienteDTO.buildPazienteDTOFromModel(paziente);
	}
//
//	// gli errori di validazione vengono mostrati con 400 Bad Request ma
//	// elencandoli grazie al ControllerAdvice
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PazienteDTO createNew(@Valid @RequestBody PazienteDTO pazienteInput) {
//		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
//		// non sta bene
		if (pazienteInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		;
		Paziente pazienteAggiornato = pazienteService.inserisciNuovo(pazienteInput.buildPazienteModel());
		return PazienteDTO
				.buildPazienteDTOFromModel(pazienteService.caricaSingoloPaziente(pazienteAggiornato.getId()));
	}

	@PutMapping("/{id}")
	public PazienteDTO update(@Valid @RequestBody PazienteDTO pazienteInput,
			@PathVariable(required = true) Long id) {
		Paziente utente = pazienteService.caricaSingoloPaziente(id);

		if (utente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + id);

		pazienteInput.setId(id);
		Paziente pazienteAggiornato = pazienteService.aggiorna(pazienteInput.buildPazienteModel());
		return PazienteDTO.buildPazienteDTOFromModel(pazienteAggiornato);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		pazienteService.rimuovi(id);
	}

	@PostMapping("/search")
	public ResponseEntity<Page<PazienteDTO>> searchPaginated(@RequestBody PazienteDTO example,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		Page<Paziente> entityPageResults = pazienteService.findByExample(example.buildPazienteModel(),
				pageNo, pageSize, sortBy);
		return new ResponseEntity<Page<PazienteDTO>>(PazienteDTO.fromModelPageToDTOPage(entityPageResults),
				HttpStatus.OK);} 
	

}
