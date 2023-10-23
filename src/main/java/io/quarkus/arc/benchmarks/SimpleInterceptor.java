package io.quarkus.arc.benchmarks;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Simple
@Priority(1)
@Interceptor
public class SimpleInterceptor {

    @AroundInvoke
    Object simpleAroundInvoke(InvocationContext ctx) throws Exception {
        return ctx.proceed() + "!";
    }
}
