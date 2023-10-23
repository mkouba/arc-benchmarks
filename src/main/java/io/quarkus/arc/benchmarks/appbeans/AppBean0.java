package io.quarkus.arc.benchmarks.appbeans;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.runtime.Startup;

@ApplicationScoped
@Startup
public class AppBean0 {
    void ping() {
    }

}