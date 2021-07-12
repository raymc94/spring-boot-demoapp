package demoapp.model;

import org.springframework.data.repository.CrudRepository;

public interface ProductoRecetaRepository extends CrudRepository<ProductoReceta, Long> {
	Iterable<ProductoReceta> findByIdReceta(Long l);
}
