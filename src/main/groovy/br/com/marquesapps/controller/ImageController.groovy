package br.com.marquesapps.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.model.Image
import br.com.marquesapps.repository.AlbumRepository
import br.com.marquesapps.repository.ImageRepository
import br.com.marquesapps.util.Amazon
import br.com.marquesapps.util.Util

import com.fasterxml.jackson.databind.ObjectMapper

@RequestMapping('/image')
@Controller
@PreAuthorize('isAuthenticated()')
class ImageController {
	
	@Value('${cloud.aws.bucketurl}')
	private String urlamazonS3;
	
	@Autowired
	private AlbumRepository albumRepository
	
	@Autowired
	private ImageRepository imageRepository
	
	@Autowired
	private Amazon amazon;
	
	@Autowired
	private Util util;
	
	@Autowired
	private MessageSource messageSource
	
	@RequestMapping(value="/upload" , method = RequestMethod.POST)
	def ResponseEntity<String> upload(@RequestParam("file") MultipartFile f,
										  @RequestParam("jsonimg") def jsonimg ,
										  @RequestParam("albumid") Long albumid	) {
	
		try {
			
			def album=albumRepository.findOne(albumid);
			ObjectMapper mapper = new ObjectMapper();
			def jsonInString = jsonimg;
			//JSON from String to Object
			Image image = mapper.readValue(jsonInString, Image.class);
			
			if (!f.isEmpty()) {
				def midia = amazon.UploadS3(f)
				image.setName(midia)
			}
			
			image.setUser(util.getLoggedUser())
			image.setAlbum(album)
			imageRepository.save(image)
			return new ResponseEntity<>([message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
				   
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model,
			 @RequestParam(value='idalbum',required=false) Long idalbum) {
		
		if (idalbum!=null && !idalbum.isEmpty() ){
			def user = util.getLoggedUser()
			def album=albumRepository.findOne(idalbum)
			def image=imageRepository.findByUserAndAlbumAndActiveTrue(user,album)
			model.addAttribute("image", image);
		}
		new ModelAndView("views/image/view")
	}
	
	@PreAuthorize('permitAll')
	@RequestMapping(value="/viewpublic",method = RequestMethod.GET)
	def viewpublic(Model model,
			       @RequestParam(value='idalbum',required=false) Long idalbum) {
		 
		 if (idalbum!=null && !idalbum.isEmpty() ){
			 def album=albumRepository.findOne(idalbum)
			 def image=imageRepository.findByAlbumAndActiveTrueAndFullpermitedTrue(album)
			 model.addAttribute("image", image);
		 }
		 new ModelAndView("views/image/view")
	}
	
	@PreAuthorize('permitAll')
	@RequestMapping(value="/searchbyalbum/{id}",method=RequestMethod.GET)
	def ResponseEntity<?> searchbyalbum(@PathVariable(value="id") Long id) {
		
		try{
			def album=albumRepository.findOne(id)
			def user=util.getLoggedUser()
			def images
			if(user==null){
			    images=imageRepository.findByAlbumAndActiveTrue(album)
			}else{
				images=imageRepository.findByUserAndAlbum(user,album)
			}
			
			if (images.size()==0){
				
			} 
			
			return new ResponseEntity<>([images:images,
										 message:messageSource.getMessage("noregister", null, LocaleContextHolder.getLocale())
										], HttpStatus.OK);
			
		}catch (Exception e) {
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
				  
	@RequestMapping(value="/image" , method = RequestMethod.GET)
	def create(Model model) {
		model.addAttribute("image", new Image());
		new ModelAndView("views/image/create")
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def ResponseEntity<String> delete(@PathVariable(value="id") Long id) {
		
		def image = imageRepository.findOne(id)
		
		try {
			imageRepository.delete(id);
			
			if (image.name){
				if (!image.name.isEmpty()){
					def delete = amazon.fileDelete (image.name)
				}
			}
			return new ResponseEntity<>([message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
			return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
				  
	@RequestMapping(value="/image" , method = RequestMethod.POST)
	@Transactional
	def ResponseEntity<String> save(@RequestParam("jsonimg") def jsonimg ,
									  @RequestParam("albumid") Long albumid	) {
		
		try {
			
				def album=albumRepository.findOne(albumid);
				ObjectMapper mapper = new ObjectMapper();
				def jsonInString = jsonimg;
				//JSON from String to Object
				Image image = mapper.readValue(jsonInString, Image.class);
				image.setUser(util.getLoggedUser())
				image.setAlbum(album)
				imageRepository.save(image)
				return new ResponseEntity<>([message:messageSource.getMessage("success", null, LocaleContextHolder.getLocale())], HttpStatus.OK);
		
		} catch (Exception e) {
				
				return new ResponseEntity<>([message:messageSource.getMessage("error", null, LocaleContextHolder.getLocale())], HttpStatus.NO_CONTENT );
		
		}
	}
}
