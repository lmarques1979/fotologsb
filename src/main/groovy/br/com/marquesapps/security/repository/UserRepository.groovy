package br.com.marquesapps.security.repository;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.security.model.User

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	Page<User> findAll(Pageable pageable);
	List<User> findByActiveTrue();
	User findByUsernameAndId(String username, Long id);
	User findByEmailAndId(String email, Long id);
	User findByUsername(String username);
	User findByEmail(String email);
}