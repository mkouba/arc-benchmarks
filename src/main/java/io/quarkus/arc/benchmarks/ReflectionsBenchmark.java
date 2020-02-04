package io.quarkus.arc.benchmarks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import io.quarkus.arc.impl.Reflections;

@BenchmarkMode(Mode.Throughput)
@Fork(5)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Benchmark)
public class ReflectionsBenchmark {

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
