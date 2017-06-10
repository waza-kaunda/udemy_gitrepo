package com.devopsbuddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CopyController {

	private static final String ABOUT_VIEW_NAME = "copy/about";

	@RequestMapping("/about")
	public String about(){
		return CopyController.ABOUT_VIEW_NAME;
	}
	
}
