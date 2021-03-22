package shukawam.examples.fido2.exception;

import com.webauthn4j.util.exception.WebAuthnException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebAuthnExceptionMapper implements ExceptionMapper<WebAuthnException> {

    private static final int BAD_REQUEST = 400;

    @Override
    public Response toResponse(WebAuthnException e) {
        return Response.status(BAD_REQUEST, e.getMessage()).build();
    }
}
