package demoapp.model;

import org.springframework.data.repository.CrudRepository;

public interface RecetaRepository extends CrudRepository<Receta, Long> {
	Iterable<Receta> findByIdOwner(Long l);
}
