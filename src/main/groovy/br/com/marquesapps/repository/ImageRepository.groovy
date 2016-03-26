package br.com.marquesapps.repository;

import java.util.List;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.model.Album
import br.com.marquesapps.model.Image
import br.com.marquesapps.model.Message;
import br.com.marquesapps.security.model.User;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
	
	Page<Image> findAll(Pageable pageable);
	
	//Find all images from selected album and user
	List<Image> findByAlbumAndActiveTrue(Album album);
	
	//Find all images from selected album and user
	List<Image> findByAlbum(Album album);
	
	//Find all publics images
	List<Image> findByAlbumAndActiveTrueAndFullpermitedTrue(Album album);
	
	//@Query(value="SELECT i FROM Message m, Image i, Album a WHERE m.image=i and i.album=a and i.album=:album order by m.datemessage DESC")
	//List<Image> findByAlbumAndActiveTrue(@Param("album") Album album)
	
	@Query(value="SELECT distinct i FROM Message m, Image i, Album a WHERE m.image=i and i.album=a and a.user=:user")
	List<Image> findByAlbum(@Param("user") User user);
	
}