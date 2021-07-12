package demoapp.unit;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import demoapp.model.Receta;
import demoapp.model.RecetaRepository;
import demoapp.model.UsuarioRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RecetaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RecetaRepository recetaRepository;

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

    @Test
    public void comprobarIgualdadSinId() {
        // GIVEN

    	Receta receta1 = new Receta();
        receta1.setIdOwner(1L);
        receta1.setNombre("Nombre de prueba");
        receta1.setDescripcion("Descripcion de prueba");
        receta1.setTypeReceta(1);
        
        Receta receta2 = new Receta();
        receta2.setIdOwner(1L);
        receta2.setNombre("Nombre de prueba");
        receta2.setDescripcion("Descripcion de prueba");
        receta2.setTypeReceta(1);
        
        
        Receta receta3 = new Receta();
        receta3.setIdOwner(1L);
        receta3.setNombre("Macarrones con tomate");
        receta3.setDescripcion("Descripcion de prueba");
        receta3.setTypeReceta(1);

        // THEN

        //assertThat(receta1).isEqualTo(receta2);
        assertThat(receta1).isNotEqualTo(receta3);
    }

    @Test
    public void comprobarIgualdadConId() {
        // GIVEN

    	Receta receta1 = new Receta();
        receta1.setIdOwner(1L);
        receta1.setNombre("Nombre de prueba");
        receta1.setDescripcion("Descripcion de prueba");
        receta1.setTypeReceta(1);
        
        Receta receta2 = new Receta();
        receta2.setIdOwner(1L);
        receta2.setNombre("Nombre de prueba");
        receta2.setDescripcion("Descripcion de prueba");
        receta2.setTypeReceta(1);
        
        
        Receta receta3 = new Receta();
        receta3.setIdOwner(1L);
        receta3.setNombre("Macarrones con tomate");
        receta3.setDescripcion("Descripcion de prueba");
        receta3.setTypeReceta(1);
        
        receta1.setId(1L);
        receta2.setId(2L);
        receta3.setId(1L);

        // THEN

        assertThat(receta1).isEqualTo(receta3);
        assertThat(receta1).isNotEqualTo(receta2);
    }

    //
    // Tests TareaRepository
    //

    @Test
    @Transactional
    public void crearTareaEnBaseDatos() {
        // GIVEN
        Receta receta = new Receta();
        receta.setIdOwner(1L);
        receta.setNombre("Nombre de prueba");
        receta.setDescripcion("Descripcion de prueba");
        receta.setTypeReceta(1);

        // WHEN

        Receta recetaObtenida = recetaRepository.save(receta);

        // THEN
        assertThat(receta).isEqualTo(recetaObtenida);
        assertThat(receta.getId()).isNotNull();
        assertThat(receta.getIdOwner()).isEqualTo(1L);
        assertThat(receta.getNombre()).isEqualTo("Nombre de prueba");
    }
}
