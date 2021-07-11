package demoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pojo.UserData;

@Controller
public class HomeController {

    @GetMapping("/")
    public String greeting() {
    	return "redirect:/login";
    }
    
}
