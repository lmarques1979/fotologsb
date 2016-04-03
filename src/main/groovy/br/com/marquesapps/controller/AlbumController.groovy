package br.com.marquesapps.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.model.Album
import br.com.marquesapps.repository.AlbumRepository
import br.com.marquesapps.util.Util

@RequestMapping('/album')
@Controller
@PreAuthorize('isAuthenticated()')
class AlbumController {
	
	@Autowired
	private AlbumRepository albumRepository
	
	@Autowired
	private MessageSource messageSource
	
	@Autowired
	private Util util
	
	@PreAuthorize('permitAll')
	@RequestMapping(value="/searchalbuns",method = RequestMethod.POST) 
	def ResponseEntity<List<Album>> searchalbuns() {
		def user = util.getLoggedUser()
		def albuns
		try {
			if (user==null){
				albuns=albumRepository.findByActiveTrueAndFullpermitedTrue()
			}else{
				albuns=albumRepository.findByUserAndActiveTrue(user)
			}
			return new ResponseEntity<>([albuns:albuns], HttpStatus.OK);
		}catch (Exception e) {
				  		
			return new ResponseEntity<>([message:messageSource.getMessage("emailerror", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
				  
		}
		
	}
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model) {
		def util = new Util()
		def user = util.getLoggedUser()
		def orderby = new Sort(new Order(Sort.Direction.DESC, "dateini"))
		def album=albumRepository.findByUser(user,orderby)
		model.addAttribute("album", album);
		new ModelAndView("views/album/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET)
	def show(Model model ,
			 @PathVariable(value="id") Long id) {
		def album=albumRepository.findOne(id)
		model.addAttribute("album", album);
		new ModelAndView("views/album/edit")
	}
				  
	@RequestMapping(value="/album" , method = RequestMethod.GET)
	def create(Model model) {
		model.addAttribute("album", new Album());
		new ModelAndView("views/album/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id) {
		albumRepository.delete(id);
		return "redirect:/album/view";
	}
				  
	@RequestMapping(value="/album" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("album") Album album,
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/album/edit"
		}else{
				
				def useralbum
				def util = new Util()
				def user = util.getLoggedUser()
				
				//Valido usuario
				useralbum = albumRepository.findByDescriptionAndUser(album.description,user)
				if (useralbum!=null){
					
						if (useralbum.getId()==null){
							bindingResult.rejectValue("description","albumexists", messageSource.getMessage("albumexists", null, LocaleContextHolder.getLocale()))
							return "views/album/edit"
						}
						
						if (useralbum.id!=album.getId()){
							bindingResult.rejectValue("description","albumexists", messageSource.getMessage("albumexists", null, LocaleContextHolder.getLocale()))
							return "views/album/edit"
						}
				}
				
				album.setUser(user)
				albumRepository.save(album)
		}
		
		new ModelAndView("views/album/create" ,
				[message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())])
	}
}
