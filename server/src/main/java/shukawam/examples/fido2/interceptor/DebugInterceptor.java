package shukawam.examples.fido2.interceptor;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Interceptor
@Debug
@Dependent
@Priority(Interceptor.Priority.APPLICATION)
public class DebugInterceptor {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object intercept(final InvocationContext invocationContext) throws Exception {
        var methodName = invocationContext.getMethod();
        logger.info(String.format("START: %s", methodName));
        Object ret;
        try {
            ret = invocationContext.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        logger.info(String.format("END: %s", methodName));
        return ret;
    }
}
