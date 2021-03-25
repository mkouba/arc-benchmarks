package io.quarkus.arc.benchmarks;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Priority(1)
@Decorator
public class PingDecorator implements Ping {

    @Inject
    @Delegate
    Ping delegate;

    @Override
    public String ping() {
        return new StringBuilder(delegate.ping()).reverse().toString();
    }
}
