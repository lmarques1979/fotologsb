package br.com.marquesapps.util;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.ui.Model

@Component
public class Pagination{
	
	@Autowired
	private Util util
	
	def getPagination(def repository, Pageable pageable , Model model, Sort orderList , int type, String search){
		
		def itensperpage=1
		def sort
		def pageimpl , pageimpl2
		def configuration=model.getAt("configuration")
		//Tipo 1 = Pageable , 2 = Tabela configuracoes banco
		//Caso não queria usar os itens por página das configurações para uma determinada página , irá usar
		//O que tiver no objeto pageable (pageable.getPageSize())
		if (type==1){
			itensperpage = pageable.getPageSize()			
		}else{
			if (configuration!=null){
				itensperpage=configuration.getItensperpage()
			}
		}
	
		def pagerequest = new PageRequest(pageable.getPageNumber(),itensperpage, orderList)
		
		pageimpl=repository.findAll(pagerequest)
			
		def i , pages=[]
		for (i=0; i < pageimpl.getTotalPages(); i++) {pages.add(i)}
		model.addAttribute("pages", pages);
		model.addAttribute("pageimpl", pageimpl);
		model.addAttribute("total", pageimpl.getTotalElements());
		
	}
}