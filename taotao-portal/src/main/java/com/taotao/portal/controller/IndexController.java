package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.ContentService;
import com.taotao.common.utils.JsonUtils;
@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	private JsonUtils jsonUtils;
	@RequestMapping("/index")
	public ModelAndView showIndex() {
	    System.out.println("sds");
	    ModelAndView mv = new ModelAndView("index");
		String adJson = contentService.getContentList();
		System.out.println(adJson);
		mv.addObject("ad1", adJson);
		
		mv.addObject("indexData",this.contentService.getIndexData() );
		
		return mv;
	}
	@RequestMapping("/products/*.html")
        public String showother(Model model) {
               System.out.println("sadsaas");
                
                return "other";
        }
	@RequestMapping(value="/httpclient/post", method=RequestMethod.POST, 
			produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String testPost(String username, String password) {
		String result = "username:" + username + "\tpassword:" + password;
		System.out.println(result);
		return "username:" + username + ",password:" + password;
	}
}
