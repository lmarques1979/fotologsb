package br.com.marquesapps.repository;

import org.springframework.data.repository.CrudRepository

import br.com.marquesapps.model.Configuration
import br.com.marquesapps.security.model.User

public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
	
	Configuration findByUser(User user);
	
}