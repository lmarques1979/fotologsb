package br.com.marquesapps.security.controller;

import javax.mail.MessagingException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailSender
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.model.Configuration
import br.com.marquesapps.repository.ConfigurationRepository
import br.com.marquesapps.security.model.Rule
import br.com.marquesapps.security.model.User
import br.com.marquesapps.security.model.UserRule
import br.com.marquesapps.security.repository.RuleRepository
import br.com.marquesapps.security.repository.UserRepository
import br.com.marquesapps.security.repository.UserRuleRepository
import br.com.marquesapps.util.Amazon
import br.com.marquesapps.util.Configurations
import br.com.marquesapps.util.Pagination
import br.com.marquesapps.util.SmtpMailSender
import br.com.marquesapps.util.Util

import com.fasterxml.jackson.databind.ObjectMapper

@RequestMapping('/user')
@Controller
@PreAuthorize('permitAll')
class UserController {
	
	@Autowired
	private Configurations configurations
	
	@Autowired
	private Util util
	
	@Autowired
	private Amazon amazon;
	
	@Autowired
	private SmtpMailSender smtpMailSender;
	
	@Autowired
	private Pagination pagination
	
	@Autowired
	private UserRepository userRepository
	
	@Autowired
	private UserRuleRepository userruleRepository
	
	@Autowired
	private RuleRepository ruleRepository
	
	@Autowired
	private ConfigurationRepository configurationRepository
	
	@Autowired
	private MessageSource messageSource
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@RequestMapping(value="/sendemail",method = RequestMethod.POST)
	def ResponseEntity<String> sendemail(@RequestParam('name') String name,
				  @RequestParam('email') String email,
				  @RequestParam('message') String message) throws MessagingException{
				  
				  try {
					  
					  def from = email;
					  def msg = messageSource.getMessage("fromsender", null , LocaleContextHolder.getLocale()) + email + "<br>"
					      msg+= messageSource.getMessage("name", null , LocaleContextHolder.getLocale()) + ': '+ name + "<br>"
						  msg+= messageSource.getMessage("mailmessage", null , LocaleContextHolder.getLocale()) + '<br><b>' + message + '</b>'
					
						  smtpMailSender.send(messageSource.getMessage("sendemailto", null, LocaleContextHolder.getLocale()),
											  from,
											  messageSource.getMessage("emailsubject", null, LocaleContextHolder.getLocale()),
											  msg);
										  
						return new ResponseEntity<>([message:messageSource.getMessage("emailsuccess", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
				  } catch (Exception e) {
				  		
				  		return new ResponseEntity<>([message:messageSource.getMessage("emailerror", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
				  
				  }
	}
	
	@RequestMapping(value="/forgotpassword",method = RequestMethod.POST)
	def forgot(@RequestParam("email") String email) throws MessagingException{
		
		def user = userRepository.findByEmail(email)
		
		if (user==null){
			return new ModelAndView("views/user/forgotpassword",
			[errors:messageSource.getMessage("emailnotexists", null, LocaleContextHolder.getLocale())])
		}else{
			
		    Random newpassword = new Random()
			String password = Math.abs(newpassword.nextInt())
			user.setPassword(new BCryptPasswordEncoder().encode(password))
			def subject=messageSource.getMessage("newpasswordsubject", null, LocaleContextHolder.getLocale())
			def msg=messageSource.getMessage("newpassword", null, LocaleContextHolder.getLocale()) + "<b>: " + password + "</b>" 
			def ret= smtpMailSender.send(email,
										 'liunit@gmail.com',
										 subject,
										 msg);
			userRepository.save(user)
		}
		
		def msg=[email]
		String[] args = msg
		new ModelAndView("views/user/forgotpassword",
			[message:messageSource.getMessage("emailsendsuccess", args , LocaleContextHolder.getLocale())])
	}
	
	@RequestMapping(value="/forgotpassword",method = RequestMethod.GET)
	def password() {
		new ModelAndView("views/user/forgotpassword")
	}
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	@PreAuthorize('hasAuthority("ADMIN")')
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuration=configurations.getUserConfiguration()
		model.addAttribute("configuration",configuration);
		def orderList = new Sort(new Order(Sort.Direction.ASC, "firstname"))
		pagination.getPagination(userRepository, pageable, model, orderList,2 , null)
		new ModelAndView("views/user/view")
	}
	
	@RequestMapping(value="/show/{id}",method = RequestMethod.GET)
	@PreAuthorize('isAuthenticated()')
	def show(Model model ,
		     @PathVariable(value = "id") Long id,
			 HttpServletResponse response) {
		
	  def user=userRepository.findOne(id)
	
	  def loggeduser = util.getLoggedUser()
	  if (loggeduser.id!=id && loggeduser.username!='admin'){
		  	model.addAttribute("errorcode", '403');
			model.addAttribute("errormessage", messageSource.getMessage("erro403", null , LocaleContextHolder.getLocale()));
			return new ModelAndView("views/error/error")
	  }
	  	   
	  model.addAttribute("user", user);
	  new ModelAndView("views/user/edit")
	}
				  
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	@PreAuthorize('isAuthenticated()')
	def delete(@PathVariable(value="id") Long id, 
		       HttpServletRequest request, 
			   HttpServletResponse response) {
		def user = userRepository.findOne(id)
		if (user.image){
			if (!user.image.isEmpty()){
				def delete = amazon.fileDelete (user.image)
			}
		}
		userRepository.delete(id);	  
		def loggeduser = util.getLoggedUser()
		if(id==loggeduser.getId()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    new SecurityContextLogoutHandler().logout(request, response, auth);
			return "redirect:/user/login?logout";
		}else{
			return "redirect:/user/view";
		}
	}
				  
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	def login(){
		return new ModelAndView("views/user/login")
    }
	
	@RequestMapping(value = "/logout" , method = RequestMethod.GET)
	def logout(HttpServletRequest request, 
			   HttpServletResponse response){
			   
	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	   new SecurityContextLogoutHandler().logout(request, response, auth);
	   return "redirect:/user/login?logout";
	}
	
	@RequestMapping(value="/user" , method = RequestMethod.GET) 
	def create(Model model) {
		model.addAttribute("user", new User());
		new ModelAndView("views/user/create")
	}
	
	@RequestMapping(value="/user" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("user") User user,  
			 BindingResult bindingResult, 
			 @RequestParam("file") MultipartFile f,
			 @RequestParam("passwordconfirm") String passwordconfirm ) {
		
		if (bindingResult.hasErrors()) {
			 return 'views/user/edit';
		}else{
		
				def userusername, useremail, userusernameid , useremailid
				def rule, userrule
								
				//Valido username
				userusername = userRepository.findByUsername(user.getUsername())	
				if (userusername!=null){
					
						if (user.getId()==null){
							bindingResult.rejectValue("username","userexists", messageSource.getMessage("userexists", null, LocaleContextHolder.getLocale()))
							return 'views/user/edit';
						}
						
						if (userusername.id!=user.getId()){
							bindingResult.rejectValue("username","userexists", messageSource.getMessage("userexists", null, LocaleContextHolder.getLocale()))
							return 'views/user/edit';
					    }						
				}
				
				//Valido email 
				useremail=userRepository.findByEmail(user.getEmail())
				if (useremail!=null){	
									
						if (user.getId()==null){
							bindingResult.rejectValue("email","emailexists", messageSource.getMessage("emailexists", null, LocaleContextHolder.getLocale()))
							return 'views/user/edit';
						}
						
						if (useremail.id!=user.getId()){
							bindingResult.rejectValue("email","email", messageSource.getMessage("emailexists", null, LocaleContextHolder.getLocale()))
							
							return 'views/user/edit';
						}
						
				}
				
				if(user.password!=passwordconfirm){
					bindingResult.rejectValue("password","password", messageSource.getMessage("passworddiferent", null, LocaleContextHolder.getLocale()))
					return 'views/user/edit';
				}
				
				if (!f.isEmpty()) {
					def midia = amazon.UploadS3(f)
					user.image = midia
				}
				user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()))
				user.username = user.username.toLowerCase()
				userRepository.save(user)
				
				if (user.username=='admin'){
					
					rule = ruleRepository.findByDescription('ADMIN')
					if (rule==null){
						rule = new Rule()
						rule.setDescription('ADMIN')
						rule.setActive(true)
						ruleRepository.save(rule)						
					}
				}else{
					rule = ruleRepository.findByDescription('USER')
					if (rule==null){
						rule = new Rule()
						rule.setDescription('USER')
						rule.setActive(true)
						ruleRepository.save(rule)
					}
				}
				
				userrule = userruleRepository.findByUserAndRule(user , rule)
				if (userrule==null){
					userrule = new UserRule()
					userrule.setRule(rule)
					userrule.setUser(user)
					userruleRepository.save(userrule)
				}
				
				def configurations=configurationRepository.findByUser(user)
				if (configurations==null){
					def configuration= new Configuration() 
					configuration.setUser(user)
					configuration.setItensperpage(10)
					configuration.setHeightimg(120)
					configuration.setWidthimg(180)
					configuration.setHeightthumbs(24)
					configuration.setWeidththumbs(24)
					configurationRepository.save(configuration)
				}
		}
		
		new ModelAndView("views/user/create" ,
			             [message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())])
	}
}
