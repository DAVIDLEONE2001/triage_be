package it.prova.triage_be.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.prova.triage_be.model.Paziente;
import it.prova.triage_be.reopsitory.PazienteRepository;

@Service
public class PazienteServiceImpl implements PazienteService {

	@Autowired
	private PazienteRepository repository;
	
	
	@Override
	public List<Paziente> listAllPazienti() {
		return (List<Paziente>) repository.findAll();
	}

	@Override
	public Paziente caricaSingoloPaziente(Long id) {

		return repository.findById(id).orElse(null);
	}

	@Override
	public Paziente aggiorna(Paziente pazienteInstance) {
		return repository.save(pazienteInstance);
	}

	@Override
	public Paziente inserisciNuovo(Paziente pazienteInstance) {
		return repository.save(pazienteInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) {
		repository.deleteById(idToRemove);

	}

	@Override
	public Page<Paziente> findByExample(Paziente example, Integer pageNo, Integer pageSize, String sortBy) {
		Specification<Paziente> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getNome()))
				predicates.add(cb.like(cb.upper(root.get("nome")), "%" + example.getNome().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCognome()))
				predicates.add(cb.like(cb.upper(root.get("cognome")), "%" + example.getCognome().toUpperCase() + "%"));
			
			if (StringUtils.isNotEmpty(example.getCodiceFiscale()))
				predicates
						.add(cb.like(cb.upper(root.get("codicefiscale")), "%" + example.getCodiceFiscale().toUpperCase() + "%"));
			
			if (StringUtils.isNotEmpty(example.getCodiceDottore()))
				predicates
				.add(cb.like(cb.upper(root.get("codicedottore")), "%" + example.getCodiceDottore().toUpperCase() + "%"));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));

			if (example.getDataRegistrazione() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataRegistrazione"), example.getDataRegistrazione()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		Pageable paging = null;
		// se non passo parametri di paginazione non ne tengo conto
		if (pageSize == null || pageSize < 10)
			paging = Pageable.unpaged();
		else
			paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return repository.findAll(specificationCriteria, paging);
	}

	@Override
	public Paziente findByCodiceFiscale(String codiceFiscale) {
		return repository.findByCodiceFiscale(codiceFiscale);
	}

}
