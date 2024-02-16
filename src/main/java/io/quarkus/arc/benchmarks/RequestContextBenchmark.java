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

    @Param({ "1", "3", "5" })
    public int beans;

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
        // Obtain the client proxies
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
        // 2. Invoke a client proxy method of some @RequestScoped beans
        // 3. Terminate the context
        requestContext.activate();
        try {
            if (beans == 1) {
                // 1 x 15 client proxy invocations
                for (int i = 0; i < 15; i++) {
                    blackhole.consume(simpleBean1.ping());
                }
            } else if (beans == 3) {
                // 3 x 5 client proxy invocations
                int loop = 5;
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean1.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean2.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean3.ping());
                }
            } else if (beans == 5) {
                // 5 x 3 client proxy invocations
                int loop = 3;
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean1.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean2.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean3.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean4.ping());
                }
                for (int i = 0; i < loop; i++) {
                    blackhole.consume(simpleBean5.ping());
                }
            }
        } finally {
            requestContext.terminate();
        }
    }

}
