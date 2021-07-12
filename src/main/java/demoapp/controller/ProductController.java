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

import demoapp.model.Producto;
import demoapp.model.Usuario;
import demoapp.service.ProductoService;
import demoapp.service.UsuarioService;
import pojo.ProductoData;

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
    	model.addAttribute("isAdmin", user.getAdministrador());
    	model.addAttribute("productos", productos);
    	
    	
        return "listaProductos";
    }
    
    @GetMapping("/productos/nuevo")
    public String formNuevoProducto(Model model,  @ModelAttribute ProductoData productoData, HttpSession session) {
    	
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "nuevoProducto";
    }
    
    @PostMapping("/productos/nuevo")
    public String nuevoProducto(@ModelAttribute ProductoData productoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

    	 Usuario usuario = usuarioService.checkUsuarioLogeado(session);
    	 
         if (usuario == null) {
         	return "redirect:/login";
         }
         
         productoService.nuevoProducto(productoData.getNombre(), productoData.getUnidad());
         flash.addFlashAttribute("mensaje", "Producto creado correctamente");

        
        return "redirect:/productos";
    }
    
    @GetMapping("/productos/{id}/editar")
    public String formEditaProducto(@PathVariable(value="id") Long idProducto, @ModelAttribute ProductoData productoData,
                                 Model model, HttpSession session) {

        Producto producto = productoService.findById(idProducto);
        
        if (producto == null) {
           return "redirect:/productos";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        productoData.setNombre(producto.getNombre());
        productoData.setUnidad(producto.getUnidad());

        model.addAttribute("productoData", productoData);
        model.addAttribute("idProducto", idProducto);
        
        return "editarProducto";
    }
    
    @PostMapping("/productos/{id}/editar")
    public String guardaEditaProducto(@PathVariable(value="id") Long idProducto, @ModelAttribute ProductoData productoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
       
    	Producto producto = productoService.findById(idProducto);
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (producto == null || usuario == null) {
        	return "redirect:/login";
        }

        productoService.modificaProducto(idProducto, productoData.getNombre(), productoData.getUnidad());
        flash.addFlashAttribute("mensaje", "Producto modificado correctamente");
        return "redirect:/productos";
    }
    
    
    @DeleteMapping("/productos/{id}")
    @ResponseBody
    public String borrarReceta(@PathVariable(value="id") Long idProducto, RedirectAttributes flash, HttpSession session) {
        Producto producto = productoService.findById(idProducto);
        Usuario user = usuarioService.checkUsuarioLogeado(session);
        
        if (producto == null || user == null) {
        	return "";
        }

        productoService.borraProducto(idProducto);
        return "";
    }
    
   
    
    
    
    
}
