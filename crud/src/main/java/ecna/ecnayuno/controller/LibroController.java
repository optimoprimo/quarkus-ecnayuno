package ecna.ecnayuno.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse;

import ecna.ecnayuno.model.Libro;
import ecna.ecnayuno.repo.LibroRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/libro")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LibroController {
    
    private LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GET
    public List<Libro> getLibros(){
        return libroRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public RestResponse<?> findById(@PathParam("id") UUID id){
        return libroRepository.findByIdOptional(id).map(RestResponse::ok).orElse(RestResponse.notFound());
    }

    @POST
    @Transactional
    public RestResponse<Void> addLibro(Libro libro, UriInfo uriInfo){
        libroRepository.persist(libro);
        return RestResponse.created(uriInfo.getBaseUriBuilder().path(LibroController.class).path(LibroController.class, "findById").build(libro.getId()));
    }
}
