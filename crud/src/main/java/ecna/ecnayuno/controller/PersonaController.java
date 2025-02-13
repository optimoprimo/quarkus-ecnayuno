package ecna.ecnayuno.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import ecna.ecnayuno.model.Persona;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.inject.Inject;
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

@Path("/persona")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PersonaController {
    
    @Inject
    private Logger log;

    @GET
    @Path("/{id}")
    public RestResponse<Persona> findById(@PathParam("id") Integer id){
        log.debug("findById");
        return Persona.<Persona>findByIdOptional(id).map(RestResponse::ok).orElse(RestResponse.notFound());
    }

    @GET
    public List<Persona> getPersonas(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("1") int pageSize,
            @QueryParam("sortBy") @DefaultValue("id") String sortBy,
            @QueryParam("direction") @DefaultValue("Ascending") Direction direction  ) {
        log.debug("getPersonas");
        return Persona.findAll(Sort.by(sortBy, direction)).page(pageNumber, pageSize).list();
    }

    @POST
    @Transactional
    public RestResponse<Void> addPersona(Persona persona, UriInfo uriInfo) {
        log.debug("addPersona");
        Persona.persist(persona);
        return RestResponse.created(uriInfo.getBaseUriBuilder().path(PersonaController.class)
                .path(PersonaController.class, "findById").build(persona.getId()));
    }

    @PUT
    @Transactional
    public RestResponse<Void> updatePersona(Persona persona) {
        log.debug("updatePersona");
        Persona.getEntityManager().merge(persona);
        return RestResponse.noContent();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public RestResponse<Void> deleteById(@PathParam("id") Integer id) {
        log.debug("deleteById");
        Persona.deleteById(id);
        return RestResponse.noContent();
    }
}
