package ecna.ecnayuno.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import ecna.ecnayuno.model.Libro;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@Repository
public class LibroRepository implements PanacheRepositoryBase<Libro, UUID>  {

    public List<Libro> findByNombre(String nombre) {
        return this.find("nombre", nombre).list();
    }
}
