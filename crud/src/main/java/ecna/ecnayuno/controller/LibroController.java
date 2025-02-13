package ecna.ecnayuno.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import ecna.ecnayuno.model.Libro;
import ecna.ecnayuno.repo.LibroRepository;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/libro")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LibroController {

    private LibroRepository libroRepository;
    private Logger log;

    public LibroController(LibroRepository libroRepository, Logger log) {
        this.libroRepository = libroRepository;
        this.log = log;
    }

    @GET
    public List<Libro> getLibros(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("1") int pageSize,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("Ascending") Direction direction  ) {
        log.debug("getLibros");
        return libroRepository.findAll(Sort.by(sortBy, direction)).page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    public RestResponse<Libro> findById(@PathParam("id") UUID id) {
        log.debug("findById");
        return libroRepository.findByIdOptional(id).map(RestResponse::ok).orElse(RestResponse.notFound());
    }

    @POST
    @Transactional
    public RestResponse<Void> addLibro(Libro libro, UriInfo uriInfo) {
        log.debug("addLibro");
        libroRepository.persist(libro);
        return RestResponse.created(uriInfo.getBaseUriBuilder().path(LibroController.class)
                .path(LibroController.class, "findById").build(libro.getId()));
    }

    @PUT
    @Transactional
    public RestResponse<Void> updateLibro(Libro libro) {
        log.debug("updateLibro");
        libroRepository.getEntityManager().merge(libro);
        return RestResponse.noContent();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public RestResponse<Void> deleteById(@PathParam("id") UUID id) {
        log.debug("deleteById");
        libroRepository.deleteById(id);
        return RestResponse.noContent();
    }

}
