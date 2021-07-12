package demoapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demoapp.model.Usuario;
import demoapp.service.UsuarioService;
import demoapp.service.UsuarioService.LoginStatus;
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
          return "redirect:/mainboard";
            
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "login";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_USER_BLOCK) {
        	model.addAttribute("error", "Usuario bloqueado");
            return "login";
        }
        
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
    	
    	
    	Boolean adminExists = usuarioService.findAdminExists();
    	
        model.addAttribute("adminExists", adminExists);
        model.addAttribute("userData", new UserData());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerSubmit(@Valid UserData userData, BindingResult result, Model model) {

    	if (result.hasErrors()) {
            return "register";
        }
    	
    	if (usuarioService.findByEmail(userData.getEmail()) != null) {
            model.addAttribute("userData", userData);
            model.addAttribute("error", "El usuario " + userData.getEmail() + " ya existe");
            return "register";
        }
    	
    	Usuario usuario = new Usuario(userData.getEmail());
        usuario.setPassword(userData.getPassword());
        usuario.setFechaNacimiento(userData.getFechaNacimiento());
        usuario.setNombre(userData.getName());
        usuario.setAdministrador(userData.getAdministrador());

        usuarioService.registrar(usuario);
        return "redirect:/login";
    	
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
         session.setAttribute("idUsuarioLogeado", null);
         return "redirect:/login";
    }
    
    
    @GetMapping("/usuario/editar")
    public String formEditaUsuario(@ModelAttribute UserData userData,
                                 Model model, HttpSession session) {

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        userData.setName(usuario.getNombre());
        userData.setEmail(usuario.getEmail());
        userData.setFechaNacimiento(usuario.getFechaNacimiento());

        model.addAttribute("userData", userData);
        
        return "editarUsuario";
    }
    
    @PostMapping("/usuario/editar")
    public String guardaEditaProducto( @ModelAttribute UserData userData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
       
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        } else if (userData.getPassword() == null || "".equals(userData.getPassword())) {
        	flash.addFlashAttribute("mensaje", "password obligatorio");
        	return "redirect:/usuario/editar";
        }
        
        usuario.setNombre(userData.getName());
        usuario.setPassword(userData.getPassword());

        usuarioService.actualizar(usuario);
        flash.addFlashAttribute("mensaje", "usuario actualizado correctamente");
        return "redirect:/productos";
    }
    
    @GetMapping("/usuarios")
    public String listadoUsuarios(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	if(!user.getAdministrador()) {
    		return "redirect:/mainboard";
    	}
    	
    	List<Usuario> usuarios = new ArrayList<Usuario>();
    	
    	usuarioService.findAllUsuarios().forEach(usuarios::add);
    	model.addAttribute("usuario", user);
    	model.addAttribute("isAdmin", user.getAdministrador());
    	model.addAttribute("usuarios", usuarios);
    	
    	
        return "listaUsuarios";
    }
    
    @GetMapping("/usuarios/{id}/bloquea")
    public String bloqueaUsuario(@PathVariable(value="id") Long idUserLock, Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	if(!user.getAdministrador()) {
    		return "redirect:/mainboard";
    	}


    	Usuario userLock = usuarioService.findById(idUserLock);
    	
    	if(userLock != null) {
    		userLock.setBloqueado(true);
    		usuarioService.actualizar(userLock);
    	}
    	
        return "redirect:/usuarios";
    }
    
    @GetMapping("/usuarios/{id}/desbloquea")
    public String desbloqueaUsuario(@PathVariable(value="id") Long idUserLock, Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	if(!user.getAdministrador()) {
    		return "redirect:/mainboard";
    	}


    	Usuario userLock = usuarioService.findById(idUserLock);
    	
    	if(userLock != null) {
    		userLock.setBloqueado(false);
    		usuarioService.actualizar(userLock);
    	}
    	
        return "redirect:/usuarios";
    }
    
    
    
}
