package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;

@Warmup(batchSize = 8192)
@Measurement(batchSize = 8192)
public class SingleDecoratorBenchmark extends BenchmarkBase {

    private PingBean bean;

    @Setup
    public void setup() {
        Arc.initialize();
        bean = Arc.container().instance(PingBean.class).get();
        if (bean == null) {
            throw new IllegalStateException("PingBean not found");
        }
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String run() throws InterruptedException {
        String ret = bean.ping();
        if (!ret.equals("ko")) {
            throw new IllegalStateException("Incorrect result: " + ret);
        }
        return ret;
    }

}
