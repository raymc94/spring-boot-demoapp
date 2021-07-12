package demoapp.controller;

import java.util.ArrayList;
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
import demoapp.model.ProductoReceta;
import demoapp.model.Receta;
import demoapp.model.Usuario;
import demoapp.service.ProductoRecetaService;
import demoapp.service.ProductoService;
import demoapp.service.RecetaService;
import demoapp.service.UsuarioService;
import pojo.ProductoRecetaData;
import pojo.ProductoRecetaId;
import pojo.RecetaData;

@Controller
public class MainboardController {
    
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RecetaService recetaService;
	
	@Autowired
	ProductoRecetaService productoRecetaService;
	
	@Autowired
	ProductoService productoService;
	
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
    public String nuevaReceta(@ModelAttribute RecetaData recetaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

    	 Usuario usuario = usuarioService.checkUsuarioLogeado(session);
    	 
         if (usuario == null) {
         	return "redirect:/login";
         }
         
         recetaService.nuevaRecetaUsuario(usuario.getId(), recetaData.getNombre(), recetaData.getDescripcion(), recetaData.getTypeReceta());
         flash.addFlashAttribute("mensaje", "Receta creada correctamente");

        
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

        recetaData.setNombre(receta.getNombre());
        recetaData.setDescripcion(receta.getDescripcion());
        recetaData.setTypeReceta(receta.getTypeReceta());
        

        model.addAttribute("receta", receta);
        return "editarReceta";
    }
    
    @GetMapping("/recetas/{id}/overview")
    public String overviewReceta(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(idReceta);
        
        if (receta == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }

        recetaData.setNombre(receta.getNombre());
        recetaData.setDescripcion(receta.getDescripcion());
        recetaData.setTypeReceta(receta.getTypeReceta());
        List<ProductoReceta> productosRel = new ArrayList<>();
        receta.getProductosAsignados().forEach(productosRel::add);
        

        model.addAttribute("usuario", usuario);
        model.addAttribute("receta", receta);
        model.addAttribute("productosRel",productosRel);
        
        return "overviewReceta";
    }
    ///recetas/{id}/ingrediente/nuevo(id=${receta.id}
    @GetMapping("/recetas/{id}/ingrediente/nuevo")
    public String addIngredienteAReceta(@PathVariable(value="id") Long idReceta, @ModelAttribute ProductoRecetaData productoRecetaData,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(idReceta);
        
        if (receta == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        List<Producto> productos = new ArrayList<>();
        productoService.allProductos().forEach(productos::add);
        
        model.addAttribute("idReceta", receta.getId());
        model.addAttribute("productos",productos);
        
        return "addIngredienteReceta";
    }
    
    @PostMapping("/recetas/ingrediente/nuevo")
    public String grabarIngredienteAReceta(@ModelAttribute ProductoRecetaData productoRecetaData,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(productoRecetaData.getIdReceta());
        Producto producto = productoService.findById(productoRecetaData.getIdProducto());
        
        if (receta == null || producto == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        ProductoReceta productoReceta = new ProductoReceta();
        productoReceta.setIdProducto(producto);
        productoReceta.setIdReceta(receta);
        productoReceta.setId(new ProductoRecetaId(receta.getId(),producto.getId()));
        productoReceta.setValor(productoRecetaData.getValor());
        
        productoRecetaService.nuevoProductoReceta(productoReceta);
        
        
        return "redirect:/recetas/"+productoRecetaData.getIdReceta()+"/overview";
    }
    
    @PostMapping("/recetas/{id}/editar")
    public String guardaEditaReceta(@PathVariable(value="id") Long idReceta, @ModelAttribute RecetaData recetaData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
       
    	Receta receta = recetaService.findById(idReceta);
        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (receta == null || usuario == null) {
        	return "redirect:/login";
        }

        recetaService.modificaReceta(idReceta, recetaData.getNombre(), recetaData.getDescripcion(), recetaData.getTypeReceta());
        flash.addFlashAttribute("mensaje", "Receta modificada correctamente");
        return "redirect:/mainboard";
    }
    
    @DeleteMapping("/recetas/{id}/ingrediente/{idProd}")
    @ResponseBody //del('/recetas/1ingrediente/1')
    public String borrarProductoReceta(@PathVariable(value="id") Long idReceta, @PathVariable(value="idProd") Long idProducto, RedirectAttributes flash, HttpSession session) {
        Receta receta = recetaService.findById(idReceta);
        Producto producto = productoService.findById(idProducto);
        Usuario user = usuarioService.checkUsuarioLogeado(session);
        
        if (receta == null || user == null) {
        	return "";
        }
        
        ProductoReceta productoReceta = new ProductoReceta();
        productoReceta.setIdProducto(producto);
        productoReceta.setIdReceta(receta);
        productoReceta.setId(new ProductoRecetaId(receta.getId(),producto.getId()));

        productoRecetaService.borraProductoReceta(productoReceta);
        return "";
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
