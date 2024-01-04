package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;

@Warmup(batchSize = 8192)
@Measurement(batchSize = 8192)
public class InterceptorBenchmark extends BenchmarkBase {

    private SimpleBean simpleBean;

    private ComplexInterceptedBean complexBean;

    @Setup
    public void setup() {
        Arc.initialize();
        simpleBean = Arc.container().instance(SimpleBean.class).get();
        if (simpleBean == null) {
            throw new IllegalStateException("SimpleBean not found");
        }
        complexBean = Arc.container().instance(ComplexInterceptedBean.class).get();
        if (complexBean == null) {
            throw new IllegalStateException("ComplexInterceptedBean not found");
        }
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String simple() throws InterruptedException {
        String ret = simpleBean.ping();
        if (!ret.equals("ok!")) {
            throw new IllegalStateException("Incorrect result: " + ret);
        }
        return ret;
    }

    @Benchmark
    public String complex() throws InterruptedException {
        String ret = complexBean.go10();
        if (!ret.equals("Hi!")) {
            throw new IllegalStateException("Incorrect result: " + ret);
        }
        return ret;
    }

}
