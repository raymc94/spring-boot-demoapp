package demoapp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Receta;
import demoapp.model.RecetaRepository;
import demoapp.model.Usuario;
import demoapp.model.UsuarioRepository;

@Service
public class RecetaService {

    Logger logger = LoggerFactory.getLogger(RecetaService.class);

    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;

    @Autowired
    public RecetaService(UsuarioRepository usuarioRepository, RecetaRepository recetaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.recetaRepository = recetaRepository;
    }

    @Transactional
    public Receta nuevaRecetaUsuario(Long idUsuario, String nombreReceta, String descripcionReceta, Integer tipoReceta) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            //throw new RecetaServiceException("Usuario " + idUsuario + " no existe al crear receta " + tituloReceta);
        }
        Receta receta = new Receta();
        receta.setIdOwner(idUsuario);
        receta.setNombre(nombreReceta);
        receta.setDescripcion(descripcionReceta);
        receta.setTypeReceta(tipoReceta);
        recetaRepository.save(receta);
        return receta;
    }

    @Transactional(readOnly = true)
    public List<Receta> allRecetasUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            //throw new RecetaServiceException("Usuario " + idUsuario + " no existe al listar recetas ");
        }
        
        
        List<Receta> recetas = new ArrayList();
        recetaRepository.findByIdOwner(idUsuario).forEach(recetas::add);
        return recetas;
    }

    @Transactional(readOnly = true)
    public Receta findById(Long recetaId) {
        return recetaRepository.findById(recetaId).orElse(null);
    }

    @Transactional
    public Receta modificaReceta(Long idReceta, String nuevoNombre, String nuevaDescripcion, Integer tipoReceta) {
        Receta receta = recetaRepository.findById(idReceta).orElse(null);
        if (receta == null) {
            //throw new RecetaServiceException("No existe receta con id " + idReceta);
        }
        receta.setNombre(nuevoNombre);
        receta.setDescripcion(nuevaDescripcion);
        receta.setTypeReceta(tipoReceta);
        recetaRepository.save(receta);
        return receta;
    }

    @Transactional
    public void borraReceta(Long idReceta) {
        Receta receta = recetaRepository.findById(idReceta).orElse(null);
        if (receta == null) {
            //throw new RecetaServiceException("No existe receta con id " + idReceta);
        }
        recetaRepository.delete(receta);
    }
}
