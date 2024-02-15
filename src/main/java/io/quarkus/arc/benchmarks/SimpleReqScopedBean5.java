package io.quarkus.arc.benchmarks;

import jakarta.enterprise.context.RequestScoped;

import io.quarkus.arc.Unremovable;

@Unremovable
@RequestScoped
public class SimpleReqScopedBean5 {

    public String ping() {
        return "ok";
    }

}
