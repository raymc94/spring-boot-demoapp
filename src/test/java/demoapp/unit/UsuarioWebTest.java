package demoapp.unit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import demoapp.model.Usuario;
import demoapp.service.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuarioOK() throws Exception {

        Usuario anaGarcia = new Usuario("rmc84@ua");
        anaGarcia.setId(1L);

        when(usuarioService.login("rmc84@ua", "123")).thenReturn(UsuarioService.LoginStatus.LOGIN_OK);
        when(usuarioService.findByEmail("rmc84@ua")).thenReturn(anaGarcia);

        this.mockMvc.perform(post("/login")
                .param("email", "rmc84@ua")
                .param("password", "123"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mainboard"));
    }

    @Test
    public void servicioLoginUsuarioNotFound() throws Exception {

        when(usuarioService.login("pepito.perez@gmail.com", "123")).thenReturn(UsuarioService.LoginStatus.USER_NOT_FOUND);

        this.mockMvc.perform(post("/login")
                    .param("email","pepito.perez@gmail.com")
                    .param("password","123"))
                .andDo(print())
                .andExpect(content().string(containsString("No existe usuario")));
    }

    @Test
    public void servicioLoginUsuarioErrorPassword() throws Exception {

        when(usuarioService.login("rmc84@ua", "000")).thenReturn(UsuarioService.LoginStatus.ERROR_PASSWORD);

        this.mockMvc.perform(post("/login")
                    .param("email","rmc84@ua")
                    .param("password","000"))
                .andDo(print())
                .andDo(print())
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginRedirectContraseñaIncorrecta() throws Exception {
        this.mockMvc.perform(get("/login")
                .flashAttr("error", "Contraseña incorrecta"))
                .andDo(print())
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginRedirectUsuarioNotFound() throws Exception {
        this.mockMvc.perform(get("/login")
                .flashAttr("error", "No existe usuario"))
                .andDo(print())
                .andExpect(content().string(containsString("No existe usuario")));
    }

}
