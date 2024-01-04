package io.quarkus.arc.benchmarks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.impl.Reflections;

@Warmup(batchSize = 8192)
@Measurement(batchSize = 8192)
public class ReflectionsBenchmark extends BenchmarkBase {

    @Benchmark
    public Field findField() throws InterruptedException {
        Field field = Reflections.findField(Foo.class, "name");
        if (field == null) {
            throw new IllegalStateException("Field not found");
        }
        return field;
    }

    @Benchmark
    public Method findMethod() throws InterruptedException {
        Method method = Reflections.findMethod(Foo.class, "getName");
        if (method == null) {
            throw new IllegalStateException("Method not found");
        }
        return method;
    }

    public static class Foo extends Bar {
    }

    public static class Bar extends Baz {
    }

    public static class Baz {

        protected String name;

        public String getName() {
            return name;
        }

    }

}
