package it.prova.triage_be.reopsitory;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.prova.triage_be.model.Paziente;


public interface PazienteRepository extends JpaSpecificationExecutor<Paziente>, PagingAndSortingRepository<Paziente, Long> {

	Paziente findByCodiceFiscale(String codiceFiscale); 
	
}
