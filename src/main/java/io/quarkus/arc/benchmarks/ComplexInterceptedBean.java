package io.quarkus.arc.benchmarks;

import javax.enterprise.context.Dependent;

import io.quarkus.arc.Unremovable;

@Unremovable
@Simple
@Dependent
public class ComplexInterceptedBean extends MySuper {

    public String ping1() {
        return "ok";
    }

    public void ping2() {
    }

    public void ping3() {
    }

}
