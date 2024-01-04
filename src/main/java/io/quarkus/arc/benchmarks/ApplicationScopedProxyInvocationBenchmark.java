package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;

@Warmup(batchSize = 8192)
@Measurement(batchSize = 8192)
public class ApplicationScopedProxyInvocationBenchmark extends BenchmarkBase {

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
