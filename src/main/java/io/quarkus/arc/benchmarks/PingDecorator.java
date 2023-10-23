package io.quarkus.arc.benchmarks;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

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
