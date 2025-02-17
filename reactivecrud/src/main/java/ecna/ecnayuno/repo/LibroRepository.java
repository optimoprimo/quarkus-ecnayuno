package ecna.ecnayuno.repo;

import java.util.List;
import java.util.UUID;

import ecna.ecnayuno.model.Libro;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LibroRepository implements PanacheRepositoryBase<Libro, UUID>  {

    public Uni<List<Libro>> findByNombre(String nombre) {
        return this.find("nombre", nombre).list();
    }
}
