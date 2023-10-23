package io.quarkus.arc.benchmarks;

import jakarta.enterprise.context.Dependent;

import io.quarkus.arc.Unremovable;

@Unremovable
@Simple
@Dependent
public class InterceptedBean extends AbstractBean {

    public String ping() {
        return "ok";
    }

}
