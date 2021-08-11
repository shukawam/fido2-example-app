package shukawam.examples.fido2.filter;

import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
@Logged
public class CustomLogFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("URI");
        logger.info(containerRequestContext.getUriInfo().getRequestUri().toString());
        logger.info("Request Header");
        containerRequestContext.getHeaders().forEach((k, v) -> logger.info(String.format("%s: %s", k, v.toString())));
        logger.info("Request Body");
        logger.info(getEntityBody(containerRequestContext));
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        logger.info("Response Header");
        containerResponseContext.getHeaders().forEach((k, v) -> logger.info(String.format("%s: %s", k, v.toString())));
        if (containerResponseContext.getEntity() == null) {
            // do nothing.
        } else {
            logger.info("Request Body");
            logger.info(containerResponseContext.getEntity().toString());
        }
    }

    private String getEntityBody(ContainerRequestContext context) {
        var out = new ByteArrayOutputStream();
        var in = context.getEntityStream();
        final var builder = new StringBuilder();
        try {
            ReaderWriter.writeTo(in, out);
            var requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                builder.append("").append("\n");
            } else {
                builder.append(new String(requestEntity)).append("\n");
            }
            context.setEntityStream(new ByteArrayInputStream(requestEntity));
        } catch (IOException e) {
            // handle logging error.
        }
        return builder.toString();
    }
}
