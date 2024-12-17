package config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title="Games Store API",
                version = "1.0.0-SNAPSHOT",
                contact = @Contact(
                        name = "Games Store API Support",
                        url = "http://exampleurl.com/contact",
                        email = "zgrinber@redhat.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class OpenApiConfig extends Application {
}
