package br.com.marquesapps.util;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import br.com.marquesapps.repository.ConfigurationRepository
import br.com.marquesapps.security.CustomUser
import br.com.marquesapps.security.repository.UserRepository

@Component
class Configurations {	
	
	@Autowired
	private UserRepository userRepository
	
	@Autowired
	private ConfigurationRepository configurationRepositorio
	
	def getUserConfiguration(){
		
		def user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		def principal=auth.getPrincipal()
		
        if (principal!='anonymousUser'){
			CustomUser userDetail = (CustomUser) auth.getPrincipal();
			user = userDetail.getUser()
        }else{
			user = userRepository.findByUsername("admin")
        }
		def configuration = configurationRepositorio.findByUser(user);		
		return configuration
	}	
} 
