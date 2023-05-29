package it.prova.triage_be.web.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import it.prova.triage_be.dto.AssegnazioneDTO;
import it.prova.triage_be.dto.DottoreResponseDTO;
import it.prova.triage_be.dto.PazienteDTO;
import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.service.PazienteService;
import it.prova.triage_be.web.api.exception.DottoreNotFoundException;
import it.prova.triage_be.web.api.exception.PazienteNotFoundException;

@RestController
@RequestMapping("/api/triage")
public class TriageController {

	private static final Logger LOGGER = LogManager.getLogger(PazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteService pazienteService;

	@CrossOrigin("*")
	@PostMapping("/assegnaPaziente")
	public PazienteDTO assegnaPaziente(@Valid @RequestBody AssegnazioneDTO input) {
		Paziente paziente = pazienteService.findByCodiceFiscale(input.getCodiceFiscalePaziente());
		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + input.getCodiceFiscalePaziente());

		LOGGER.info("....invocazione servizio esterno....con Codice Dottore: " + input.getCodiceDottore());
		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verifica/" + input.getCodiceDottore()).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");

		if (dottoreResponseDTO.isNotValid()) {

			throw new DottoreNotFoundException("Dottore not valid con codice " + input.getCodiceDottore());

		}

		if (dottoreResponseDTO.getInServizio() == true && dottoreResponseDTO.getInVisita() == false) {
			paziente.setCodiceDottore(dottoreResponseDTO.getCodiceDottore());

			AssegnazioneDTO assegnazione = AssegnazioneDTO.builder().codiceDottore(input.getCodiceDottore())
					.codiceFiscalePaziente(input.getCodiceFiscalePaziente()).build();
			LOGGER.info("....invocazione servizio esterno....");
			webClient.post().uri("/impostaInVisita").bodyValue(assegnazione).retrieve()
					.bodyToMono(AssegnazioneDTO.class).block();
			LOGGER.info("....invocazione servizio esterno terminata....");

		}
		Paziente pazienteAggiornato = pazienteService.aggiorna(paziente);
		return PazienteDTO.buildPazienteDTOFromModel(pazienteService.caricaSingoloPaziente(pazienteAggiornato.getId()));
	}

	// CRUD DOTTORE

	@GetMapping("/dottore")
	public List<DottoreResponseDTO> getAll() {
		// senza DTO qui hibernate dava il problema del N + 1 SELECT
		// (probabilmente dovuto alle librerie che serializzano in JSON)
		LOGGER.info("....invocazione servizio esterno....con Codice Dottore: ");
		List<DottoreResponseDTO> dottoriResponseDTO = webClient.get().uri("/").retrieve().bodyToMono(List.class)
				.block();
		LOGGER.info("....invocazione servizio esterno terminata....");

		return dottoriResponseDTO;
	}

	@GetMapping("/dottore/{codiceDottore}")
	public DottoreResponseDTO findByCodice(@PathVariable(value = "codiceDottore", required = true) String codiceDottore) {

		LOGGER.info("....invocazione servizio esterno....con Codice Dottore: ");
		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/get/" + codiceDottore).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");

		return dottoreResponseDTO;
	}



	@PostMapping("/dottore/search")
	public ResponseEntity<Page<DottoreResponseDTO>> searchPaginated(@RequestBody DottoreResponseDTO example,
	        @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
	        @RequestParam(defaultValue = "id") String sortBy) {

	    LOGGER.info("....invocazione servizio esterno....");

	    ResponseEntity<Page<DottoreResponseDTO>> responseEntity = webClient.post()
	            .uri("/search?pageNo=" + pageNo + "&pageSize=" + pageSize + "&sortBy=" + sortBy).bodyValue(example)
	            .retrieve().toEntity(new ParameterizedTypeReference<Page<DottoreResponseDTO>>() {}).block();
	    LOGGER.info("....invocazione servizio esterno terminata....");

	    
	    return responseEntity;
	}

	

}
