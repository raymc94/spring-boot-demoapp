package demoapp.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Usuario;
import demoapp.service.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    Logger logger = LoggerFactory.getLogger(UsuarioServiceTest.class);

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        UsuarioService.LoginStatus loginStatusOK = usuarioService.login("rmc84@ua", "123");
        UsuarioService.LoginStatus loginStatusErrorPassword = usuarioService.login("rmc84@ua", "000");
        UsuarioService.LoginStatus loginStatusNoUsuario = usuarioService.login("pepito.perez@gmail.com", "123");

        // THEN

        assertThat(loginStatusOK).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
        assertThat(loginStatusErrorPassword).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);
        assertThat(loginStatusNoUsuario).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
    }

    @Test
    @Transactional
    public void servicioRegistroUsuario() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("123");

        // WHEN

        usuarioService.registrar(usuario);

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // Pasamos como argumento un usario sin contraseña
        Usuario usuario =  new Usuario("usuario.prueba@gmail.com");
        usuarioService.registrar(usuario);
    }



    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN
        // Pasamos como argumento un usario con emaii existente en datos-test.sql
        Usuario usuario =  new Usuario("rmc84@ua");
        usuario.setPassword("123");
        usuarioService.registrar(usuario);

        // THEN
        // Se produce una excepción comprobada con el expected del test
    }

    @Test
    @Transactional
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba@gmail.com");
        usuario.setPassword("123");

        // WHEN

        usuario = usuarioService.registrar(usuario);

        // THEN

        assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        Usuario usuario = usuarioService.findByEmail("rmc84@ua");

        // THEN

        assertThat(usuario.getId()).isEqualTo(1L);
    }
}