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

@BenchmarkMode(Mode.Throughput)
@Fork(5)
@Warmup(iterations = 5, time = 1, batchSize = 8192)
@Measurement(iterations = 5, time = 1, batchSize = 8192)
@State(Scope.Benchmark)
public class InterceptorBenchmark {

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
