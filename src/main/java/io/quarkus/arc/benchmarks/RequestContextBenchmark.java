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
public class RequestContextBenchmark extends BenchmarkBase {

    private ManagedContext requestContext;
    private SimpleReqScopedBean simpleReqScopedBean;
    
    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        requestContext = container.requestContext();
        simpleReqScopedBean = container.select(SimpleReqScopedBean.class).get();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String run() throws InterruptedException {
        // 1. Activate the context
        // 2. Use a @RequestScoped bean
        // 3. Terminate the context
        requestContext.activate();
        try {
            return simpleReqScopedBean.ping();
        } finally {
            requestContext.terminate();
        }
    }

}
