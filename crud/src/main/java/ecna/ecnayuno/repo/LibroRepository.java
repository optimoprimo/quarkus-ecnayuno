package ecna.ecnayuno.repo;

import java.util.UUID;

import ecna.ecnayuno.model.Libro;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LibroRepository implements PanacheRepositoryBase<Libro, UUID>  {
}
