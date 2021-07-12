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
import demoapp.model.RecetaFavorita;
import demoapp.model.Usuario;
import demoapp.service.ProductoRecetaService;
import demoapp.service.ProductoService;
import demoapp.service.RecetaFavoritaService;
import demoapp.service.RecetaService;
import demoapp.service.UsuarioService;
import pojo.ProductoRecetaData;
import pojo.ProductoRecetaId;
import pojo.RecetaData;
import pojo.RecetaFavoritaId;

@Controller
public class MainboardController {
    
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RecetaService recetaService;
	
	@Autowired
	ProductoRecetaService productoRecetaService;
	
	@Autowired
	RecetaFavoritaService recetaFavoritaService;
	
	@Autowired
	ProductoService productoService;
	
    @GetMapping("/mainboard")
    public String mainboard(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	List<Receta> recetas = recetaService.allRecetasUsuario(user.getId());
    	List<Receta> recetasFavoritas = new ArrayList<Receta>();
    	user.getRecetasFavoritas().forEach(r -> recetasFavoritas.add(r.getIdReceta()));
    	model.addAttribute("usuario", user);
    	model.addAttribute("filterMode", 1);
    	model.addAttribute("recetas", recetas);
    	model.addAttribute("recetasFavoritas", recetasFavoritas);
    	
    	
        return "listaRecetas";
    }
    
    @GetMapping("/recetas/publicas")
    public String listaRecetasPublicas(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	List<Receta> recetas = recetaService.allRecetasPublicas();
    	List<Receta> recetasFavoritas = new ArrayList<Receta>();
    	user.getRecetasFavoritas().forEach(r -> recetasFavoritas.add(r.getIdReceta()));
    	model.addAttribute("usuario", user);
    	model.addAttribute("filterMode", 2);
    	model.addAttribute("recetas", recetas);
    	model.addAttribute("recetasFavoritas", recetasFavoritas);
    	
    	
        return "listaRecetas";
    }
    
    @GetMapping("/recetas/favoritas")
    public String listaRecetasFavoritas(Model model, HttpSession session) {
    	
    	Usuario user = usuarioService.checkUsuarioLogeado(session);

    	if (user == null) {
    		return "redirect:/login";
    	} 
    	
    	List<Receta> recetas = new ArrayList<Receta>();
    	user.getRecetasFavoritas().forEach(r -> recetas.add(r.getIdReceta()));
    	model.addAttribute("usuario", user);
    	model.addAttribute("filterMode", 3);
    	model.addAttribute("recetas", recetas);
    	model.addAttribute("recetasFavoritas", recetas);
    	
    	
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

    @GetMapping("/recetas/{id}/favorita/{origen}")
    public String agregaFavorito(@PathVariable(value="id") Long idReceta, @PathVariable(value="origen") Integer origen,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(idReceta);
        
        if (receta == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        RecetaFavorita recetaFavorita = new RecetaFavorita();
        RecetaFavoritaId favId = new RecetaFavoritaId();
        favId.setIdReceta(receta.getId());
        favId.setIdUsuario(usuario.getId());
        recetaFavorita.setId(favId);
        recetaFavorita.setIdReceta(receta);
        recetaFavorita.setIdUsuario(usuario);
        
        recetaFavoritaService.nuevaRecetaFavorita(recetaFavorita);
        
        if(origen == 3) {
        	return "redirect:/recetas/favoritas";
        } else if (origen == 2) {
        	return "redirect:/recetas/publicas";
        } else {
        	return "redirect:/mainboard";
        }
    }
    
    @GetMapping("/recetas/{id}/favorita/quitar/{origen}")
    public String quitaFavorito(@PathVariable(value="id") Long idReceta, @PathVariable(value="origen") Integer origen,
                                 Model model, HttpSession session) {

        Receta receta = recetaService.findById(idReceta);
        
        if (receta == null) {
           return "redirect:/mainboard";
        }

        Usuario usuario = usuarioService.checkUsuarioLogeado(session);
        
        if (usuario == null) {
        	return "redirect:/login";
        }
        
        RecetaFavorita recetaFavorita = new RecetaFavorita();
        RecetaFavoritaId favId = new RecetaFavoritaId();
        favId.setIdReceta(receta.getId());
        favId.setIdUsuario(usuario.getId());
        recetaFavorita.setId(favId);
        recetaFavorita.setIdReceta(receta);
        recetaFavorita.setIdUsuario(usuario);
        
        recetaFavoritaService.borraRecetaFavorita(recetaFavorita);
        
        if(origen == 3) {
        	return "redirect:/recetas/favoritas";
        } else if (origen == 2) {
        	return "redirect:/recetas/publicas";
        } else {
        	return "redirect:/mainboard";
        }
        
    }
    
    
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
