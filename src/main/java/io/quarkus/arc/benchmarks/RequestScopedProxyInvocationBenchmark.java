package io.quarkus.arc.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.ManagedContext;

@Warmup(batchSize = 8192)
@Measurement(batchSize = 8192)
public class RequestScopedProxyInvocationBenchmark extends BenchmarkBase {

    private SimpleReqScopedBean reqBean;

    // this state object ensures that the request context is activate for each run
    // we do not want to activate/terminate the request contex per each run but merely test the proxy invocation itself
    @State(Scope.Thread)
    public static class ContextActivator {

        private ManagedContext requestContext;

        @Setup
        public void doSetup() {
            requestContext = Arc.container().requestContext();
            requestContext.activate();
        }

        @TearDown
        public void doTearDown() {
            requestContext.terminate();
        }

    }

    @Setup
    public void setup() {
        ArcContainer container = Arc.initialize();
        reqBean = container.instance(SimpleReqScopedBean.class).get();
        if (reqBean == null) {
            throw new IllegalStateException("SimpleReqScopedBean not found");
        }
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public String run(ContextActivator contextActivator) throws InterruptedException {
        String ret = reqBean.ping();
        if (!ret.equals("ok")) {
            throw new IllegalStateException("Incorrect result: " + ret);
        }
        return ret;
    }

}
