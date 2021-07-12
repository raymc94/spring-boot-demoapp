package demoapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import demoapp.model.Producto;
import demoapp.model.Usuario;
import demoapp.service.ProductoService;
import demoapp.service.UsuarioService;

@Controller
public class ProductController {
    
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ProductoService productoService;
	
    @GetMapping("/productos")
    public String listadoProductos(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	List<Producto> productos = productoService.allProductos();
    	model.addAttribute("usuario", user);
    	model.addAttribute("productos", productos);
    	
    	
        return "listaProductos";
    }
    
//    @GetMapping("/productos/nuevo")
//    public String formNuevoProducto(Model model,  @ModelAttribute RecetaData recetaData, HttpSession session) {
//    	
//        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
//        
//        if (usuario == null) {
//        	return "redirect:/login";
//        }
//        model.addAttribute("usuario", usuario);
//        return "nuevaReceta";
//    }
//    
//    @PostMapping("/productos/nuevo")
//    public String nuevoProducto(@ModelAttribute RecetaData recetaData,
//                             Model model, RedirectAttributes flash,
//                             HttpSession session) {
//
//    	 Usuario usuario = usuarioService.checkUsuarioLogeado(session);
//    	 
//         if (usuario == null) {
//         	return "redirect:/login";
//         }
//         
//         recetaService.nuevaRecetaUsuario(usuario.getId(), recetaData.getNombre(), recetaData.getDescripcion(), recetaData.getTypeReceta());
//         flash.addFlashAttribute("mensaje", "Receta creada correctamente");
//
//        
//        return "redirect:/mainboard";
//    }
//    
//    @GetMapping("/productos/{id}/editar")
//    public String formEditaProducto(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
//                                 Model model, HttpSession session) {
//
//        Receta receta = recetaService.findById(idReceta);
//        
//        if (receta == null) {
//           return "redirect:/productos";
//        }
//
//        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
//        
//        if (usuario == null) {
//        	return "redirect:/login";
//        }
//
//        model.addAttribute("receta", receta);
//        recetaData.setNombre(receta.getNombre());
//        recetaData.setDescripcion(receta.getDescripcion());
//        recetaData.setTypeReceta(receta.getTypeReceta());
//        return "editarReceta";
//    }
//    
//    @PostMapping("/productos/{id}/editar")
//    public String guardaEditaProducto(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
//                                       Model model, RedirectAttributes flash, HttpSession session) {
//       
//    	Receta receta = recetaService.findById(idReceta);
//        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
//        
//        if (receta == null || usuario == null) {
//        	return "redirect:/login";
//        }
//
//        recetaService.modificaReceta(idReceta, recetaData.getNombre(), recetaData.getDescripcion(), recetaData.getTypeReceta());
//        flash.addFlashAttribute("mensaje", "Receta modificada correctamente");
//        return "redirect:/mainboard";
//    }
//    
//    
//    @DeleteMapping("/productos/{id}")
//    @ResponseBody
//    public String borrarReceta(@PathVariable(value="id") Long idReceta, RedirectAttributes flash, HttpSession session) {
//        Receta receta = recetaService.findById(idReceta);
//        Usuario user = usuarioService.checkUsuarioLogeado(session);
//        
//        if (receta == null || user == null) {
//        	return "";
//        }
//
//        recetaService.borraReceta(idReceta);
//        return "";
//    }
    
   
    
    
    
    
}
