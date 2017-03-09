package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转controller
 * <p>Title: PageController</p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
@Controller
public class PageController {

	/**
	 * 打开首页
	 */
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	/**
	 * 展示其他页面
	 * <p>Title: showpage</p>
	 * <p>Description: </p>
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showpage(@PathVariable String page) {
		return page;
	}
}
