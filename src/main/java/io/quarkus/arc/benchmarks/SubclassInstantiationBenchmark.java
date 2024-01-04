package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;

@Warmup(batchSize = 512)
@Measurement(batchSize = 512)
public class SubclassInstantiationBenchmark extends BenchmarkBase {

    private ArcContainer container;

    @Setup
    public void setup() {
        container = Arc.initialize();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String simpleSubclass() throws InterruptedException {
        String ret = container.instance(InterceptedBean.class).get().ping();
        if (!ret.equals("ok!")) {
            throw new IllegalStateException("Incorrect ping result: " + ret);
        }
        return ret;
    }

    @Benchmark
    public String complexSubclass() throws InterruptedException {
        String ret = container.instance(ComplexInterceptedBean.class).get().ping1();
        if (!ret.equals("ok!")) {
            throw new IllegalStateException("Incorrect ping result: " + ret);
        }
        return ret;
    }

}
