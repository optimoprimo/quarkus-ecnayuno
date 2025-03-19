Quarkus Annotations

Anotaciones de Inyección de Dependencias

````java
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MiServicio {
    
    @Inject
    @Qualifier("elOtroServicio")
    private OtroServicio otroServicio;
    
    public String procesar() {
        return otroServicio.obtenerMensaje();
    }
}
````

Posibles scopes

````java
@ApplicationScoped 
@Singleton
@RequestScoped
@Singleton
@Dependent
````

Eventos
````java 
@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {               
        LOGGER.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {               
        LOGGER.info("The application is stopping...");
    }

}
````


Anotaciones para Servicios REST

````java
@Path("/hola")
public class HolaRecurso {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hola() {
        return "Hola, Quarkus!";
    }
}
````

Diferentes métodos HTTP

````java
@GET
@POST
@PUT
@DELETE
````

Especifican los tipos de contenido que produce o consume un servicio.

````java
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
````

Anotaciones de Configuración

````java
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfiguracionServicio {
    
    @ConfigProperty(name = "mi.propiedad")
    String miPropiedad;
}
````

Transacciones

````java
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioBD {
    
    @Transactional
    public void guardarEntidad(Entidad entidad) {
        em.persist(entidad);
    }
}
````
