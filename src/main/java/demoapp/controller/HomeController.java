package demoapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import demoapp.model.Usuario;
import demoapp.service.UsuarioService;

@Controller
public class HomeController {

	@Autowired
	UsuarioService usuarioService;
	
    @GetMapping("/")
    public String greeting() {
    	return "redirect:/login";
    }
    
    @GetMapping("/about")
    public String loginForm(Model model, HttpSession session) {
		
		Usuario usuario = null;
		
		if(session.getAttribute("idUsuarioLogeado") != null) {
			Long idUsuario = (Long )session.getAttribute("idUsuarioLogeado");
			
			usuario = usuarioService.findById(idUsuario);
		}
		
        model.addAttribute("usuario", usuario);
		
        return "acercaDe";
    }
    
}
