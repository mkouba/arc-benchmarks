package io.quarkus.arc.benchmarks;

import javax.enterprise.context.RequestScoped;

import io.quarkus.arc.Unremovable;

@RequestScoped
@Unremovable
public class RequestBean {

    public String ping() {
        return "ok";
    }

}
