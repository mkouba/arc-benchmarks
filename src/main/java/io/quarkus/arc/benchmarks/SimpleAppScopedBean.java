package io.quarkus.arc.benchmarks;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import io.quarkus.arc.Unremovable;

@Unremovable
@ApplicationScoped
public class SimpleAppScopedBean {

    public String ping() {
        return "ok";
    }

}
