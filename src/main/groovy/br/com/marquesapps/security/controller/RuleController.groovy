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
import br.com.marquesapps.security.model.Rule
import br.com.marquesapps.security.repository.RuleRepository
import br.com.marquesapps.util.Configurations
import br.com.marquesapps.util.Pagination

@RequestMapping('/rule')
@Controller
@PreAuthorize('hasAuthority("ADMIN")')
class RuleController {
	
	@Autowired
	private Configurations configurations
	
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
		def orderList = new Sort(new Order(Sort.Direction.ASC, "description"))
		pagination.getPagination(ruleRepository, pageable, model,orderList,2 , null)
		new ModelAndView("views/rule/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {
		def rule=ruleRepository.findOne(id)
		model.addAttribute("rule", rule);
		new ModelAndView("views/rule/edit")		
	}
				  
	@RequestMapping(value="/rule" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("rule", new Rule());
		new ModelAndView("views/rule/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id) {		
		ruleRepository.delete(id);
		return "redirect:/rule/view";
	}
				  
	@RequestMapping(value="/rule" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("rule") Rule rule, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/rule/edit" 
		}else{
				
				def ruledescription
				//Valido descricao
				ruledescription = ruleRepository.findByDescription(rule.getDescription())
				if (ruledescription!=null){
					
						if (ruledescription.getId()==null){
							bindingResult.rejectValue("description","ruleexists", messageSource.getMessage("ruleexists", null, LocaleContextHolder.getLocale()))
							return "views/rule/edit"
						}
						
						if (ruledescription.id!=rule.getId()){
							bindingResult.rejectValue("description","ruleexists", messageSource.getMessage("ruleexists", null, LocaleContextHolder.getLocale()))
							return "views/rule/edit"
						}
				}
			    rule.description = rule.description.toUpperCase()
				ruleRepository.save(rule)
		}
		
		new ModelAndView("views/rule/create" ,
			    [message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())])
	}
}
