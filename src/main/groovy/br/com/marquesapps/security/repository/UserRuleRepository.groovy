package br.com.marquesapps.security.repository;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.security.model.Rule
import br.com.marquesapps.security.model.User
import br.com.marquesapps.security.model.UserRule

public interface UserRuleRepository extends PagingAndSortingRepository<UserRule, Long> {
	
	Page<UserRule> findAll(Pageable pageable);
	UserRule findByUser(User user);
	UserRule findByRule(Rule rule);
	UserRule findByUserAndRule(User user , Rule rule);
	
}