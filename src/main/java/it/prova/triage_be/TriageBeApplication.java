package it.prova.triage_be;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.model.Ruolo;
import it.prova.triage_be.model.Utente;
import it.prova.triage_be.service.PazienteService;
import it.prova.triage_be.service.RuoloService;
import it.prova.triage_be.service.UtenteService;

@SpringBootApplication
public class TriageBeApplication implements CommandLineRunner {

	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private PazienteService pazienteService;

	public static void main(String[] args) {
		SpringApplication.run(TriageBeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.err.println("********************************");
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", Ruolo.ROLE_ADMIN));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_SUB_OPERATOR) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Classic User", Ruolo.ROLE_SUB_OPERATOR));
		}

		// a differenza degli altri progetti cerco solo per username perche' se vado
		// anche per password ogni volta ne inserisce uno nuovo, inoltre l'encode della
		// password non lo
		// faccio qui perche gia lo fa il service di utente, durante inserisciNuovo
		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = Utente.builder().nome("Mario").cognome("Rossi").username("admin").password("admin")
					.dataRegistrazione(LocalDate.now())
					.ruoli(new HashSet<>(Arrays.asList(
							ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN))))
					.build();
			utenteServiceInstance.inserisciNuovo(admin);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(admin.getId());
			Paziente p1 = Paziente.builder().codiceFiscale("marco").build();
			pazienteService.inserisciNuovo(p1);
		}

		if (utenteServiceInstance.findByUsername("user") == null) {
			Utente classicUser = Utente.builder().nome("Antonio").cognome("Verdi").username("user").password("user")
					.dataRegistrazione(LocalDate.now()).ruoli(new HashSet<>()).build();
			classicUser.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_SUB_OPERATOR));
			utenteServiceInstance.inserisciNuovo(classicUser);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser.getId());

		}

		if (utenteServiceInstance.findByUsername("user1") == null) {
			Utente classicUser1 = Utente.builder().nome("Antonioo").cognome("Verdii").username("user1")
					.password("user1").dataRegistrazione(LocalDate.now()).ruoli(new HashSet<>()).build();
			classicUser1.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic User", Ruolo.ROLE_SUB_OPERATOR));
			utenteServiceInstance.inserisciNuovo(classicUser1);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser1.getId());

		}

		System.err.println("********************************");

	}

}
