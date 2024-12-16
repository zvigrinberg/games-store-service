package exceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class ExceptionsHandler {

    @ServerExceptionMapper
    public RestResponse<String> mapNotFoundException(NotFoundException e) {
        return RestResponse.status(Response.Status.NOT_FOUND, e.getMessage());
    }

    @ServerExceptionMapper
    public RestResponse<String> mapBadRequestException(BadRequestException e) {
        return RestResponse.status(Response.Status.BAD_REQUEST, e.getMessage());
    }
}


