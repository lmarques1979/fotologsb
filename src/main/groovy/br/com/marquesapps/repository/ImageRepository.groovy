package br.com.marquesapps.repository;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.model.Album
import br.com.marquesapps.model.Image
import br.com.marquesapps.security.model.User

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
	
	Page<Image> findAll(Pageable pageable);
	
	//Find all images from selected album and user
	List<Image> findByUserAndAlbumAndActiveTrue(User user, Album album);
	
	//Find all images from selected album and user
	List<Image> findByUserAndAlbum(User user, Album album);
	
	//Find all publics images
	List<Image> findByAlbumAndActiveTrueAndFullpermitedTrue(Album album);
	
	List<Image> findByAlbumAndActiveTrue(Album album);
	
	Album findByDescriptionAndUser(String description, User user);
	
}