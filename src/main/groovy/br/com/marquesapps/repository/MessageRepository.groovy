package br.com.marquesapps.repository;

import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.model.Image
import br.com.marquesapps.model.Message

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
	
	//Find all messages from selected image
	List<Message> findByImageAndActiveTrue(Image image); 
	
}