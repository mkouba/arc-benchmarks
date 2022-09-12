package io.quarkus.arc.benchmarks;

import javax.enterprise.context.RequestScoped;

import io.quarkus.arc.Unremovable;

@Unremovable
@RequestScoped
public class SimpleReqScopedBean {

    public String ping() {
        return "ok";
    }

}
