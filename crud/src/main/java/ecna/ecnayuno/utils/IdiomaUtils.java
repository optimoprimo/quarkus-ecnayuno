package ecna.ecnayuno.utils;

import java.util.Locale;

import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class IdiomaUtils {

    @Inject
    private RoutingContext routingContext;

    public Locale getLocale(){
        return  Locale.forLanguageTag(routingContext.preferredLanguage().tag());
    }
}
