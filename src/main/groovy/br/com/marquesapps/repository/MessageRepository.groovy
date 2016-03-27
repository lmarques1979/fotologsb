package br.com.marquesapps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.model.Image
import br.com.marquesapps.model.Message
import br.com.marquesapps.security.model.User

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	
	//Find all messages from selected image
	List<Message> findByImageAndActiveTrue(Image image); 
	
}