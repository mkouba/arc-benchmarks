package io.quarkus.arc.benchmarks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;

@Warmup(batchSize = 1024)
@Measurement(batchSize = 1024)
public class BeanManagerGetBeansBenchmark extends BenchmarkBase {

    @Setup
    public void setup() {
        Arc.initialize();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public Set<?> run() throws InterruptedException {
        Set<?> beans = Arc.container().beanManager().getBeans(PingBean.class);
        assertEquals(1, beans.size());
        return beans;
    }

}
