package io.quarkus.arc.benchmarks;

import javax.inject.Singleton;

import io.quarkus.arc.Unremovable;

@Unremovable
@Simple
@Singleton
public class SimpleBean {

    public String ping() {
        return "ok";
    }

}
