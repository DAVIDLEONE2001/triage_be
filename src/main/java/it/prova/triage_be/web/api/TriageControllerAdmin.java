package it.prova.triage_be.web.api;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage_be.dto.DottoreResponseDTO;

@RestController
@RequestMapping("/api/triage/admin")
public class TriageControllerAdmin {

	private static final Logger LOGGER = LogManager.getLogger(PazienteController.class);

	@Autowired
	private WebClient webClient;


	@PutMapping("/dottore/{codiceDottore}")
	public DottoreResponseDTO update(@Valid @RequestBody DottoreResponseDTO dottoreInput,
			@PathVariable(required = true) String codiceDottore) {

		LOGGER.info("....invocazione servizio esterno....");
		DottoreResponseDTO dottoreResponseDTO = webClient.put().uri("/aggiorna/" + codiceDottore)
				.bodyValue(dottoreInput).retrieve().bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");

		return dottoreResponseDTO;

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DottoreResponseDTO createNewDottore(@Valid @RequestBody DottoreResponseDTO dottoreInput) {
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		LOGGER.info("....invocazione servizio esterno....");
		DottoreResponseDTO dottoreResponseDTO = webClient.post().uri("/")
				.bodyValue(dottoreInput).retrieve().bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");
		
		return dottoreResponseDTO;
		
	}

	@DeleteMapping("/dottore/delete/{codiceDottore}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCodice(@PathVariable(required = true) String codiceDottore) {

		LOGGER.info("....invocazione servizio esterno....");
		webClient.delete().uri("/delete/" + codiceDottore).retrieve().bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");

	}

}
