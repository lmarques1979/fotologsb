package br.com.marquesapps.security.repository;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.security.model.Rule

public interface RuleRepository extends PagingAndSortingRepository<Rule, Long> {
	
	Page<Rule> findAll(Pageable pageable);
	List<Rule> findByActiveTrue();
	Rule findByDescription(String description);
	Rule findById(Long id); 
	
}