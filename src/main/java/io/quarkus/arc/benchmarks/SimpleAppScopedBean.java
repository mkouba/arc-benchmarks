package io.quarkus.arc.benchmarks;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.arc.Unremovable;

@Unremovable
@ApplicationScoped
public class SimpleAppScopedBean {

    public String ping() {
        return "ok";
    }

}
