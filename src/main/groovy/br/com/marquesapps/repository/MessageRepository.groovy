package br.com.marquesapps.repository;

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.model.Image
import br.com.marquesapps.model.Message
import br.com.marquesapps.security.model.User

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	
	//Find all messages from selected image
	List<Message> findByImageAndActiveTrue(Image image); 
	
	//@Query(value="SELECT m FROM Message m, Image i, Album a WHERE m.image=i and i.album=a and a.user=:user")
	//List<Message> findByUser(@Param("user") User user);
	
}