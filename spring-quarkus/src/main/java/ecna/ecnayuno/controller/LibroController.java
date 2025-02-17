package ecna.ecnayuno.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecna.ecnayuno.model.Libro;
import ecna.ecnayuno.repo.LibroRepository;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;

@RestController
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private Logger log;

    @GetMapping
    public List<Libro> getLibros(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "01") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "Ascending") Direction direction) {
        log.debug("getLibros");
        return libroRepository.findAll(Sort.by(sortBy, direction)).page(pageNumber, pageSize).list();
    }

    @GetMapping("/{id}")
    public RestResponse<Libro> findById(@PathVariable("id") UUID id) {
        log.debug("findById");
        return libroRepository.findByIdOptional(id).map(RestResponse::ok).orElse(RestResponse.notFound());
    }

    @PostMapping
    @Transactional
    public RestResponse<Void> addLibro(Libro libro, UriInfo uriInfo) {
        log.debug("addLibro");
        libroRepository.persist(libro);
        return RestResponse.created(uriInfo.getBaseUriBuilder().path(LibroController.class)
                .path(LibroController.class, "findById").build(libro.getId()));
    }

    @PutMapping
    @Transactional
    public RestResponse<Void> updateLibro(Libro libro) {
        log.debug("updateLibro");
        libroRepository.getEntityManager().merge(libro);
        return RestResponse.noContent();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RestResponse<Void> deleteById(@PathVariable("id") UUID id) {
        log.debug("deleteById");
        libroRepository.deleteById(id);
        return RestResponse.noContent();
    }

}
