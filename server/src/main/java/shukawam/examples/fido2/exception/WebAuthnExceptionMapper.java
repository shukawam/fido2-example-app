package shukawam.examples.fido2.exception;

import com.webauthn4j.util.exception.WebAuthnException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebAuthnExceptionMapper implements ExceptionMapper<WebAuthnException> {

    @Override
    public Response toResponse(WebAuthnException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
