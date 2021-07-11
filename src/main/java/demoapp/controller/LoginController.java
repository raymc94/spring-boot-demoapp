package demoapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demoapp.model.Usuario;
import demoapp.service.UsuarioService;
import pojo.UserData;

@Controller
public class LoginController {
    
	@Autowired
	UsuarioService usuarioService;
	
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("userData", new UserData());
        return "login";
    }
    
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute UserData userData, Model model, RedirectAttributes flash, HttpSession session) {

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(userData.getEmail(), userData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            Usuario usuario = usuarioService.findByEmail(userData.getEmail());

          session.setAttribute("idUsuarioLogeado", usuario.getId());
          //redirigimos al mainboard
          //return "redirect:/mainboard";
            
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "login";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }
        
        return "login";
    }
    
    
    
    
}