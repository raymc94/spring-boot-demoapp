package demoapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Producto;
import demoapp.model.ProductoReceta;
import demoapp.model.ProductoRecetaRepository;
import demoapp.model.ProductoRepository;
import demoapp.model.UsuarioRepository;

@Service
public class ProductoRecetaService {

    Logger logger = LoggerFactory.getLogger(ProductoRecetaService.class);

    private UsuarioRepository usuarioRepository;
    private ProductoRepository productoRepository;

    private ProductoRecetaRepository productoRecetaRepository;
    
    @Autowired
    public ProductoRecetaService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository, ProductoRecetaRepository productoRecetaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.productoRecetaRepository = productoRecetaRepository;
    }

    @Transactional
    public ProductoReceta nuevoProductoReceta(ProductoReceta productoReceta) {

        productoRecetaRepository.save(productoReceta);
        return productoReceta;
    }
    
    @Transactional(readOnly = true)
    public List<ProductoReceta> todosProductosReceta(Long idReceta) {
    	
    	
        
        List<ProductoReceta> productos = new ArrayList<>();
        productoRecetaRepository.findByIdReceta(idReceta).forEach(productos::add);
        //productoRecetaRepository.findAll().forEach(productos::add);
        return productos;
    }

    @Transactional(readOnly = true)
    public List<Producto> allProductos() {
        
        List<Producto> productos = new ArrayList<>();
        productoRepository.findAll().forEach(productos::add);
        return productos;
    }

    @Transactional(readOnly = true)
    public Producto findById(Long productoId) {
        return productoRepository.findById(productoId).orElse(null);
    }

    @Transactional
    public Producto modificaProducto(Long idProducto, String nuevoNombre, String nuevaUnidad) {
        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto == null) {
            //throw new ProductoServiceException("No existe producto con id " + idProducto);
        }
        producto.setNombre(nuevoNombre);
        producto.setUnidad(nuevaUnidad);
        productoRepository.save(producto);
        return producto;
    }

    @Transactional
    public void borraProductoReceta(ProductoReceta productoReceta) {
    	
        productoRecetaRepository.delete(productoReceta);
    }
}
