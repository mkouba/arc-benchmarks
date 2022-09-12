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
public class ClientProxyInvocationBenchmark {

    private SimpleAppScopedBean appBean;
    private SimpleReqScopedBean reqBean;
    private ManagedContext requestContext;

    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        appBean = container.instance(SimpleAppScopedBean.class).get();
        if (appBean == null) {
            throw new IllegalStateException("SimpleAppScopedBean not found");
        }
        reqBean = container.instance(SimpleReqScopedBean.class).get();
        if (reqBean == null) {
            throw new IllegalStateException("SimpleAppScopedBean not found");
        }
        requestContext = container.requestContext();
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String run() throws InterruptedException {
        String appRet = appBean.ping();
        if (!appRet.equals("ok")) {
            throw new IllegalStateException("Incorrect result: " + appRet);
        }
        try {
            requestContext.activate();
            String reqRet = reqBean.ping();
            if (!reqRet.equals("ok")) {
                throw new IllegalStateException("Incorrect result: " + reqRet);
            }
        } finally {
            requestContext.terminate();
        }
        return appRet;
    }

}
