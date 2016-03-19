package br.com.marquesapps.security.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.data.web.PageableDefault
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

import br.com.marquesapps.repository.ConfigurationRepository
import br.com.marquesapps.security.model.UserRule
import br.com.marquesapps.security.repository.RuleRepository
import br.com.marquesapps.security.repository.UserRepository
import br.com.marquesapps.security.repository.UserRuleRepository
import br.com.marquesapps.util.Configurations
import br.com.marquesapps.util.Pagination

@RequestMapping('/userrule')
@Controller 
@PreAuthorize('hasAuthority("ADMIN")')
class UserRuleController {
	
	@Autowired
	private Configurations configurations
	
	@Autowired
	private UserRuleRepository userruleRepository
	
	@Autowired
	private UserRepository userRepository
	
	@Autowired
	private RuleRepository ruleRepository
	
	@Autowired
	private Pagination pagination
	
	@Autowired
	private ConfigurationRepository configurationRepository
	
	@Autowired
	private MessageSource messageSource
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuration=configurations.getUserConfiguration()
		model.addAttribute("configuration",configuration);
		def orderList = new Sort(new Order(Sort.Direction.ASC, "user.firstname"))
		pagination.getPagination(userruleRepository, pageable, model, orderList,2, null)
		new ModelAndView("views/userrule/view")
	}
			 
	@RequestMapping(value="/show/{id}",method = RequestMethod.GET)
	def show(Model model , @PathVariable(value="id") Long id) {
		def userrule=userruleRepository.findOne(id);
		model.addAttribute("users", userRepository.findByActiveTrue());
		model.addAttribute("rules", ruleRepository.findByActiveTrue());
		model.addAttribute("userrule", userrule);
		new ModelAndView("views/userrule/edit")		
	}
				  
	@RequestMapping(value="/userrule" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("userrule", new UserRule());
		model.addAttribute("users", userRepository.findByActiveTrue());
		model.addAttribute("rules", ruleRepository.findByActiveTrue());		
		return "views/userrule/create"
	}
	
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	def delete(	@PathVariable(value="id") Long id) {
		userruleRepository.delete(id);
		return "redirect:/userrule/view";
	}
				  
	@RequestMapping(value="/userrule" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("userrule") UserRule userrule,
			 BindingResult bindingResult,
			 Model model) {
		
		if (bindingResult.hasErrors()) {
				return "views/userrule/edit"  
		}else{
				
				def useruserrule
				//Valido regra x usuario
				useruserrule = userruleRepository.findByUserAndRule(userrule.getUser() , userrule.getRule())
				if (useruserrule!=null){
					
						if (useruserrule.getId()==null){
							bindingResult.rejectValue("user","userruleexists", messageSource.getMessage("userruleexists", null, LocaleContextHolder.getLocale()))
							model.addAttribute("users", userRepository.findByActiveTrue());
							model.addAttribute("rules", ruleRepository.findByActiveTrue());
							return "views/userrule/edit" 
						}
						
						if (useruserrule.id!=userrule.getId()){
							bindingResult.rejectValue("usuario","userruleexists", messageSource.getMessage("userruleexists", null, LocaleContextHolder.getLocale()))
							model.addAttribute("users", userRepository.findByActiveTrue());
							model.addAttribute("rules", ruleRepository.findByActiveTrue());
							return "views/userrule/edit" 
						}
				}
				
				userruleRepository.save(userrule)			
		}
		
		return "redirect:/userrule/view";
	}
}
