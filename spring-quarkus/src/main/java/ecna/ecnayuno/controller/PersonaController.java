package ecna.ecnayuno.controller;

import java.util.List;

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

import ecna.ecnayuno.model.Persona;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;

@RequestMapping("/persona")
@RestController
public class PersonaController {

    @Autowired
    private Logger log;

    @GetMapping("/{id}")
    public RestResponse<Persona> findById(@PathVariable("id") Integer id) {
        log.debug("findById");
        return Persona.<Persona>findByIdOptional(id).map(RestResponse::ok).orElse(RestResponse.notFound());
    }

    @GetMapping
    public List<Persona> getPersonas(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "01") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "Ascending") Direction direction) {
        log.debug("getPersonas");
        return Persona.findAll(Sort.by(sortBy, direction)).page(pageNumber, pageSize).list();
    }

    @PostMapping
    @Transactional
    public RestResponse<Void> addPersona(Persona persona, UriInfo uriInfo) {
        log.debug("addPersona");
        Persona.persist(persona);
        return RestResponse.created(uriInfo.getBaseUriBuilder().path(PersonaController.class)
                .path(PersonaController.class, "findById").build(persona.getId()));
    }

    @PutMapping
    @Transactional
    public RestResponse<Void> updatePersona(Persona persona) {
        log.debug("updatePersona");
        Persona.getEntityManager().merge(persona);
        return RestResponse.noContent();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RestResponse<Void> deleteById(@PathVariable("id") Integer id) {
        log.debug("deleteById");
        Persona.deleteById(id);
        return RestResponse.noContent();
    }
}
