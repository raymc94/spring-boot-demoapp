package demoapp.unit;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Receta;
import demoapp.model.RecetaFavorita;
import demoapp.model.RecetaFavoritaRepository;
import demoapp.model.RecetaRepository;
import demoapp.model.Usuario;
import demoapp.model.UsuarioRepository;
import pojo.RecetaFavoritaId;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RecetaFavoritaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RecetaRepository recetaRepository;
    
    @Autowired
    RecetaFavoritaRepository recetaFavoritaRepository;

    //
    // Tests modelo Tarea
    //

    @Test
    public void crearTarea() throws Exception {

        // WHEN

        Receta receta = new Receta();
        receta.setIdOwner(1L);
        receta.setNombre("Nombre de prueba");
        receta.setDescripcion("Descripcion de prueba");
        receta.setTypeReceta(1);
        

        // THEN

        assertThat(receta.getNombre()).isEqualTo("Nombre de prueba");
        assertThat(receta.getDescripcion()).isEqualTo("Descripcion de prueba");
        assertThat(receta.getTypeReceta()).isEqualTo(1);
        assertThat(receta.getIdOwner()).isEqualTo(1L);
    }

    //
    // Tests TareaRepository
    //

    @Test
    @Transactional
    public void crearTareaEnBaseDatos() {
        // GIVEN
    	Receta receta = recetaRepository.findById(1L).get();

        Usuario usuario = usuarioRepository.findById(1L).get();
 
        RecetaFavorita recetaFavorita = new RecetaFavorita();
        RecetaFavoritaId favId = new RecetaFavoritaId();
        favId.setIdReceta(receta.getId());
        favId.setIdUsuario(usuario.getId());
        recetaFavorita.setId(favId);
        recetaFavorita.setIdReceta(receta);
        recetaFavorita.setIdUsuario(usuario);
        // WHEN

        RecetaFavorita recetaObtenida = recetaFavoritaRepository.save(recetaFavorita);

        // THEN
        assertThat(recetaFavorita.getId().getIdReceta()).isEqualTo(recetaObtenida.getId().getIdReceta());
        assertThat(recetaFavorita.getId().getIdUsuario()).isEqualTo(recetaObtenida.getId().getIdUsuario());
    }
}
