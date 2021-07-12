package demoapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demoapp.model.Receta;
import demoapp.model.Usuario;
import demoapp.service.RecetaService;
import demoapp.service.UsuarioService;
import pojo.RecetaData;

@Controller
public class MainboardController {
    
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RecetaService recetaService;
	
    @GetMapping("/mainboard")
    public String mainboard(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	List<Receta> recetas = recetaService.allRecetasUsuario(user.getId());
    	model.addAttribute("usuario", user);
    	model.addAttribute("recetas", recetas);
    	
    	
        return "listaRecetas";
    }
    
    @GetMapping("/recetas/nueva")
    public String formNuevaReceta(Model model,  @ModelAttribute RecetaData recetaData, HttpSession session) {
    	
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "nuevaReceta";
    }
    
    @PostMapping("/recetas/nueva")
    public String nuevaTarea(@ModelAttribute RecetaData recetaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

    	 Usuario usuario = usuarioService.checkUsuarioLogeado(session);
    	 
         if (usuario == null) {
         	return "redirect:/login";
         }
         
         recetaService.nuevaRecetaUsuario(usuario.getId(), recetaData.getNombre(), recetaData.getDescripcion());
         flash.addFlashAttribute("mensaje", "Tarea creada correctamente");

        
        return "redirect:/mainboard";
    }
    
    @GetMapping("/recetas/{id}/editar")
    public String formEditaReceta(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(idReceta);
        
        if (receta == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }

        model.addAttribute("receta", receta);
        recetaData.setNombre(receta.getNombre());
        recetaData.setDescripcion(receta.getDescripcion());
        return "editarReceta";
    }
    
    @PostMapping("/recetas/{id}/editar")
    public String guardaEditaReceta(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
       
    	Receta receta = recetaService.findById(idReceta);
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (receta == null || usuario == null) {
        	return "redirect:/login";
        }

        recetaService.modificaReceta(idReceta, recetaData.getNombre(), recetaData.getDescripcion());
        flash.addFlashAttribute("mensaje", "Receta modificada correctamente");
        return "redirect:/mainboard";
    }
    
    
    @DeleteMapping("/recetas/{id}")
    @ResponseBody
    public String borrarReceta(@PathVariable(value="id") Long idReceta, RedirectAttributes flash, HttpSession session) {
        Receta receta = recetaService.findById(idReceta);
        Usuario user = usuarioService.checkUsuarioLogeado(session);
        
        if (receta == null || user == null) {
        	return "";
        }

        recetaService.borraReceta(idReceta);
        return "";
    }
    
   
    
    
    
    
}
