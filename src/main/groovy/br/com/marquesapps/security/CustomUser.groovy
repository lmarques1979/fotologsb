package br.com.marquesapps.security;

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import br.com.marquesapps.security.model.User

public class CustomUser implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	private User user
	private String name
	
	Set<GrantedAuthority> authorities=null
	
	public User getUser() {
		return user
	}

	public void setUser(User user) {
		this.user = user
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities)
	{
		this.authorities=authorities;
	}
	
	public boolean isAccountNonExpired() {
		return true
	}

	public boolean isAccountNonLocked() {
		return true
	}

	public boolean isCredentialsNonExpired() {
		return true
	}

	public boolean isEnabled() {
		return true
	}

	public String getUsername() {
		return user.getUsername()
	}

	public String getPassword() {
		return user.getPassword()
	}
	
	public String getName(){
		return this.name
	}
	
	public String setName(){
		if (user.getSurname().isEmpty()){
			this.name = user.getFirstname()
		}else{
			this.name = user.getFirstname() + ' ' + user.getSurname()
		}
	}
}