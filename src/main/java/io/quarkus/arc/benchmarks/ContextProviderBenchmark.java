package io.quarkus.arc.benchmarks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.eclipse.microprofile.context.spi.ThreadContextController;
import org.eclipse.microprofile.context.spi.ThreadContextSnapshot;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.ManagedContext;
import io.quarkus.arc.runtime.context.ArcContextProvider;

@Warmup(batchSize = 1024)
@Measurement(batchSize = 1024)
public class ContextProviderBenchmark extends BenchmarkBase {

    private ArcContainer container;
    private ArcContextProvider contextProvider;

    @Setup
    public void setup() {
        container = Arc.initialize();
        contextProvider = new ArcContextProvider();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public ThreadContextController captureAndRestore() throws InterruptedException {
        // Activate the request context
        ManagedContext requestContext = container.requestContext();
        requestContext.activate();
        // Make sure the request scoped bean is created
        assertEquals("ok", container.instance(RequestBean.class).get().ping());
        try {
            ThreadContextSnapshot snapshot = contextProvider.currentContext(Map.of());
            ThreadContextController controller = snapshot.begin();
            controller.endContext();
            return controller;
        } finally {
            requestContext.terminate();
        }
    }

}
