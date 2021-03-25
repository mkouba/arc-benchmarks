package io.quarkus.arc.benchmarks;

import javax.inject.Singleton;

import io.quarkus.arc.Unremovable;

@Unremovable
@Singleton
public class PingBean implements Ping {

    public String ping() {
        return "ok";
    }

}
