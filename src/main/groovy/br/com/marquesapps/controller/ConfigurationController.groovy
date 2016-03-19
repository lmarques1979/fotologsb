package br.com.marquesapps.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
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

import br.com.marquesapps.model.Configuration
import br.com.marquesapps.repository.ConfigurationRepository
import br.com.marquesapps.util.Util

@RequestMapping('/configuration')
@Controller
@PreAuthorize('isAuthenticated()') 
class ConfigurationController {
	
	@Autowired
	private ConfigurationRepository configurationRepository
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model) {
		def util = new Util()
		def user = util.getLoggedUser()
		def configuration=configurationRepository.findByUser(user)
		model.addAttribute("configuration", configuration);
		new ModelAndView("views/configuration/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {				
		def configuration=configurationRepository.findOne(id)
		model.addAttribute("configuration", configuration);
		new ModelAndView("views/configuration/edit")		
	}
				  
	@RequestMapping(value="/configuration" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("configuration", new Configuration());
		new ModelAndView("views/configuration/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id) {		
		configurationRepository.delete(id);	
		return "redirect:/configuration/view";
	}
				  
	@RequestMapping(value="/configuration" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("configuration") Configuration configuration, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/configuration/edit" 
		}else{
				
				def userconfiguration
				def util = new Util()
				def user = util.getLoggedUser()
				//Valido usuario
				userconfiguration = configurationRepository.findByUser(user)
				if (userconfiguration!=null){
					
						if (userconfiguration.getId()==null){
							bindingResult.rejectValue("itensperpage","userconfigexists", messageSource.getMessage("userconfigexists", null, LocaleContextHolder.getLocale()))
							return "views/configuration/edit"
						}
						
						if (userconfiguration.id!=configuration.getId()){
							bindingResult.rejectValue("itensperpage","userconfigexists", messageSource.getMessage("userconfigexists", null, LocaleContextHolder.getLocale()))
							return "views/configuration/edit" 
						}
				}
				configuration.user = user
				configurationRepository.save(configuration)
		}
		
		new ModelAndView("views/configuration/create" ,
			    [message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())])
	}
}
