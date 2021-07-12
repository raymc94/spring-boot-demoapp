package demoapp.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RecetaRepository extends CrudRepository<Receta, Long> {
	Iterable<Receta> findByIdOwner(Long l);
	
	@Query(value = "SELECT * FROM RECETA WHERE TYPE_RECETA = 2", nativeQuery = true)
	Iterable<Receta> findPublicRecetas();
}
