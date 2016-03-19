package br.com.marquesapps.repository;

import java.util.List;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.model.Album
import br.com.marquesapps.model.Image;
import br.com.marquesapps.security.model.User

public interface AlbumRepository extends PagingAndSortingRepository<Album, Long> {
	
	Page<Album> findAll(Pageable pageable);
	List<Album> findByUserAndActiveTrue(User user);
	List<Album> findByUser(User user);
	Album findByDescriptionAndUser(String description, User user);
	
	//Find all publics albuns
	List<Album> findByActiveTrueAndFullpermitedTrue();
}