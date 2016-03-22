package br.com.marquesapps.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

import br.com.marquesapps.model.Image
import br.com.marquesapps.model.Message
import br.com.marquesapps.repository.ImageRepository
import br.com.marquesapps.repository.MessageRepository
import br.com.marquesapps.util.Util

import com.fasterxml.jackson.databind.ObjectMapper

@RequestMapping('/message')
@Controller
@PreAuthorize('isAuthenticated()')
class MessageController {
	
	@Autowired
	private MessageRepository messageRepository
	
	@Autowired
	private ImageRepository imageRepository
	
	@Autowired
	private MessageSource messageSource			
	
	@Autowired
	private Util util;
	
	@PreAuthorize('permitAll')
	@RequestMapping(value="/messagesimage/{id}",method=RequestMethod.GET)
	def ResponseEntity<Image> messagesimage(@PathVariable(value="id") Long id) {
		
		try {
				def image=imageRepository.findOne(id)
				def message=messageRepository.findByImageAndActiveTrue(image)
				return new ResponseEntity<>([message:message], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
	
	@RequestMapping(value="/saveactive" , method = RequestMethod.POST)
	@Transactional
	def ResponseEntity<String> saveactive(@RequestParam("jsonmsg") def jsonmsg) {
		
		try {
				ObjectMapper mapper = new ObjectMapper();
				def jsonInString = jsonmsg;
				//JSON from String to Object
				Message message = mapper.readValue(jsonInString, Message.class);
				messageRepository.save(message)
				return new ResponseEntity<>([message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
				return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
									
	@RequestMapping(value="/inactive",method=RequestMethod.GET)
	def inactive() {
		return "views/message/inactive"
	}
	
	@RequestMapping(value="/searchinactive",method=RequestMethod.POST)
	def ResponseEntity<Message> searchinactive() {
		
		try {
			def user=util.getLoggedUser()
			def message=messageRepository.findByUser(user)
			return new ResponseEntity<>([messages:message], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
	
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET)
	def ResponseEntity<Image> show(@PathVariable(value="id") Long id) {
		
		try {
			def image=imageRepository.findOne(id)
			return new ResponseEntity<>([image:image,
				                         urlimage:urlamazonS3], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def ResponseEntity<String> delete(@PathVariable(value="id") Long id) {
		
		try {
			messageRepository.delete(id);
			return new ResponseEntity<>([message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
	
	@PreAuthorize('permitAll')
	@RequestMapping(value="/message" , method = RequestMethod.POST)
	@Transactional
	def ResponseEntity<String> save(@RequestParam("jsonimg") def jsonimg ,
									@RequestParam("imageid") Long imageid	) {
		
		try {
				def image=imageRepository.findOne(imageid);
				ObjectMapper mapper = new ObjectMapper();
				def jsonInString = jsonimg;
				//JSON from String to Object
				Message message = mapper.readValue(jsonInString, Message.class);
				message.setImage(image)
				message.setDatemessage(new Date())
				messageRepository.save(message)
				return new ResponseEntity<>([message:messageSource.getMessage("successautorized", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
				return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
}
