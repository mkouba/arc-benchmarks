package io.quarkus.arc.benchmarks;

import jakarta.inject.Singleton;

import io.quarkus.arc.Unremovable;

@Unremovable
@Singleton
public class PingBean implements Ping {

    public String ping() {
        return "ok";
    }

}
