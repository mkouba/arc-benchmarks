package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.ManagedContext;

@Warmup(batchSize = 1024)
@Measurement(batchSize = 1024)
public class RequestContextActivationBenchmark extends BenchmarkBase {

    private ManagedContext requestContext;

    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        requestContext = container.requestContext();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public boolean run() throws InterruptedException {
        requestContext.activate();
        try {
            return requestContext.isActive();
        } finally {
            requestContext.terminate();
        }
    }

}
