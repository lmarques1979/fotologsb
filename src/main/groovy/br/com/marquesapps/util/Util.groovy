package br.com.marquesapps.util;

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

import br.com.marquesapps.security.CustomUser

@Component
public class Util {	
	
	def getLoggedUser(){
		
		def user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth.getPrincipal()=='anonymousUser'){
			user=null
		}else{
        	CustomUser userDetail = (CustomUser) auth.getPrincipal();
			user = userDetail.getUser()
		}
		return user
	}	
} 
