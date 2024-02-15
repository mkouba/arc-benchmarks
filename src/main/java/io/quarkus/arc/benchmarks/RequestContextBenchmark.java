package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.ManagedContext;

@Warmup(batchSize = 1024)
@Measurement(batchSize = 1024)
public class RequestContextBenchmark extends BenchmarkBase {

    @Param({ "1", "10", "100" })
    public int work;

    private ManagedContext requestContext;
    private SimpleReqScopedBean1 simpleBean1;
    private SimpleReqScopedBean2 simpleBean2;
    private SimpleReqScopedBean3 simpleBean3;
    private SimpleReqScopedBean4 simpleBean4;
    private SimpleReqScopedBean5 simpleBean5;

    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        requestContext = container.requestContext();
        simpleBean1 = container.select(SimpleReqScopedBean1.class).get();
        simpleBean2 = container.select(SimpleReqScopedBean2.class).get();
        simpleBean3 = container.select(SimpleReqScopedBean3.class).get();
        simpleBean4 = container.select(SimpleReqScopedBean4.class).get();
        simpleBean5 = container.select(SimpleReqScopedBean5.class).get();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public void run(Blackhole blackhole) throws InterruptedException {
        // 1. Activate the context
        // 2. Invoke a client proxy method of a @RequestScoped bean
        // 3. Terminate the context
        requestContext.activate();
        try {
            for (int i = 0; i < work; i++) {
                blackhole.consume(simpleBean1.ping());
                blackhole.consume(simpleBean2.ping());
                blackhole.consume(simpleBean3.ping());
                blackhole.consume(simpleBean4.ping());
                blackhole.consume(simpleBean5.ping());
            }
        } finally {
            requestContext.terminate();
        }
    }

}
