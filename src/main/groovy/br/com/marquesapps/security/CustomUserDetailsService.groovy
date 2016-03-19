package br.com.marquesapps.security;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import br.com.marquesapps.security.model.User
import br.com.marquesapps.security.model.UserRule
import br.com.marquesapps.security.repository.UserRepository
 
@Service
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {

				
		try{
			User user = userRepository.findByUsername(username);
			def rules = user.getRules();
			
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			for(UserRule userrule: rules){
				authorities.add(new SimpleGrantedAuthority(userrule.getRule().getDescription()));
			}
			
			CustomUser customuser=new CustomUser()
	        customuser.setUser(user)
	        customuser.setAuthorities(authorities)
			customuser.setName()
			return customuser
		} catch (UsernameNotFoundException e) {
				
			return e;
		
		}

	}
			
}