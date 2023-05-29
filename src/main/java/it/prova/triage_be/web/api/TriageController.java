package it.prova.triage_be.web.api;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage_be.dto.AssegnazioneDTO;
import it.prova.triage_be.dto.DottoreResponseDTO;
import it.prova.triage_be.dto.PazienteDTO;
import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.service.PazienteService;
import it.prova.triage_be.web.api.exception.PazienteNotFoundException;

@RestController
@RequestMapping("/api/triage")
public class TriageController {

	private static final Logger LOGGER = LogManager.getLogger(PazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteService pazienteService;

	@PostMapping("/assegnaPaziente")
	public PazienteDTO createNew(@Valid @RequestBody AssegnazioneDTO input) {
		Paziente paziente = pazienteService.findByCodiceFiscale(input.getCodiceFiscalePaziente());
		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + input.getCodiceFiscalePaziente());

		LOGGER.info("....invocazione servizio esterno....con Codice Dottore: " + input.getCodiceDottore());
		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verifica/" + input.getCodiceDottore()).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();
		LOGGER.info("....invocazione servizio esterno terminata....");

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

}
