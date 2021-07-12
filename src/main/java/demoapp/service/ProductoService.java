package demoapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Producto;
import demoapp.model.ProductoRepository;
import demoapp.model.UsuarioRepository;

@Service
public class ProductoService {

    Logger logger = LoggerFactory.getLogger(ProductoService.class);

    private UsuarioRepository usuarioRepository;
    private ProductoRepository productoRepository;

    @Autowired
    public ProductoService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto nuevoProducto(String nombreProducto, String unidadProducto) {

        Producto producto = new Producto();
        producto.setNombre(nombreProducto);
        producto.setUnidad(unidadProducto);
        productoRepository.save(producto);
        return producto;
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
    public void borraProducto(Long idProducto) {
    	Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto == null) {
            //throw new ProductoServiceException("No existe producto con id " + idProducto);
        }
        productoRepository.delete(producto);
    }
}
