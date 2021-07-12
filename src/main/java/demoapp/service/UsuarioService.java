package demoapp.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Usuario;
import demoapp.model.UsuarioRepository;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, ERROR_USER_BLOCK}

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else {
        	if(usuario.get().getBloqueado() != null  && usuario.get().getBloqueado() == true) {
        		return LoginStatus.ERROR_USER_BLOCK;
        	} else {
        		return LoginStatus.LOGIN_OK;
        	}
        }
    }

    @Transactional
    public Usuario registrar(Usuario usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent()) {
        	//throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        } else if (usuario.getEmail() == null) {
        	//throw new UsuarioServiceException("El usuario no tiene email");
        } else if (usuario.getPassword() == null) {
        	//throw new UsuarioServiceException("El usuario no tiene password");
        } else {
        	return usuarioRepository.save(usuario);
        }
        
        //TODO QUIT
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Iterable<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Boolean findAdminExists() {
    	Iterable<Usuario> usuarios = usuarioRepository.findAll();
    	Boolean ret = false;
    	
    	for(Usuario usuario: usuarios) {
    		if(usuario.getAdministrador() != null && usuario.getAdministrador() == true) {
    			ret = true;
    		}
    	}
    	
    	return ret;
    }
    
    public Usuario checkUsuarioLogeado(HttpSession session) {
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        
        if(idUsuarioLogeado == null) {
        	return null;
        } else {
        	Usuario user = findById(idUsuarioLogeado);
        	return user;
        }
            
    }
}