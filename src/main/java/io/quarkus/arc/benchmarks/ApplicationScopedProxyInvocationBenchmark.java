package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.ManagedContext;

@BenchmarkMode(Mode.Throughput)
@Fork(5)
@Warmup(iterations = 5, time = 1, batchSize = 8192)
@Measurement(iterations = 5, time = 1, batchSize = 8192)
@State(Scope.Benchmark)
public class ApplicationScopedProxyInvocationBenchmark {

    private SimpleAppScopedBean appBean;

    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        appBean = container.instance(SimpleAppScopedBean.class).get();
        if (appBean == null) {
            throw new IllegalStateException("SimpleAppScopedBean not found");
        }
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String run() throws InterruptedException {
        String ret = appBean.ping();
        if (!ret.equals("ok")) {
            throw new IllegalStateException("Incorrect result: " + ret);
        }
        return ret;
    }

}
