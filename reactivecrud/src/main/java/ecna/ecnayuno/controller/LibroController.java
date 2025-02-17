package ecna.ecnayuno.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import ecna.ecnayuno.model.Libro;
import ecna.ecnayuno.repo.LibroRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.smallrye.mutiny.Uni;
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
    public Uni<List<Libro>> getLibros(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("1") int pageSize,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("Ascending") Direction direction
            ) {
        log.debug("getLibros");
        return libroRepository.findAll(Sort.by(sortBy, direction)).page(pageNumber, pageSize).list();
    }

    @GET
    @Path("/{id}")
    public Uni<RestResponse<Libro>> findById(@PathParam("id") UUID id) {
        log.debug("findById");
        return libroRepository.findById(id)
        .onItem().ifNotNull().transform(RestResponse::ok)
        .onItem().ifNull().continueWith(RestResponse.notFound());
    }

    @POST
    @WithTransaction
    public Uni<RestResponse<Void>> addLibro(Libro libro, UriInfo uriInfo) {
        log.debug("addLibro");
        return libroRepository.persist(libro).map((persisted)->RestResponse.created(uriInfo.getBaseUriBuilder().path(LibroController.class)
        .path(LibroController.class, "findById").build(persisted.getId())));
    }

    @PUT
    @WithTransaction
    public Uni<RestResponse<Void>> updateLibro(Libro libro) {
        log.debug("updateLibro");
        return libroRepository.findById(libro.getId()).onItem().ifNotNull().invoke(entity -> {
            entity.setNombre(libro.getNombre());
        }).onItem().ifNotNull().call(libroRepository::persist).onItem().transform(entity -> RestResponse.noContent());
    }

    @DELETE
    @Path("/{id}")
    @WithTransaction
    public Uni<RestResponse<Void>> deleteById(@PathParam("id") UUID id) {
        log.debug("deleteById");
        return  libroRepository.deleteById(id).onItem().transform(delete -> RestResponse.noContent());
    }

}
