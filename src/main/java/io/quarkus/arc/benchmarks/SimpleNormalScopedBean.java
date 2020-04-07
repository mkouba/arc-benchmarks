package io.quarkus.arc.benchmarks;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import io.quarkus.arc.Unremovable;

@Unremovable
@ApplicationScoped
public class SimpleNormalScopedBean {

    public String ping() {
        return "ok";
    }

}
