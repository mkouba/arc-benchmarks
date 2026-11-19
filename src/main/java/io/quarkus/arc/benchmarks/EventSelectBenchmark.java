package io.quarkus.arc.benchmarks;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import io.quarkus.arc.Arc;
import io.quarkus.arc.Unremovable;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class EventSelectBenchmark {

    private BeanManager beanManager;
    private Event<Object> event;
    private Event<Payload<?>> genericEvent;
    private Event<Payload<String>> typedEvent;
    private Event<Observed> observedEvent;
    private final Signal signal = new Signal();

    @Setup
    public void setup() {
        Arc.initialize();
        Emitter emitter = Arc.container().instance(Emitter.class).get();
        event = emitter.event;
        genericEvent = emitter.genericEvent;
        typedEvent = emitter.typedEvent;
        observedEvent = emitter.observedEvent;
        beanManager = Arc.container().beanManager();
        if (beanManager.resolveObserverMethods(new Unrelated()).size() != 1
                || !beanManager.resolveObserverMethods(signal).isEmpty()) {
            throw new IllegalStateException("Unexpected observer resolution for the benchmark fixtures");
        }
    }

    @TearDown
    public void tearDown() {
        Arc.shutdown();
    }

    @Benchmark
    public void repeatedTypeLiteral() {
        genericEvent.select(new TypeLiteral<Payload<String>>() {}).fire(new Payload<>());
    }

    @Benchmark
    public void directTypedEvent() {
        typedEvent.fire(new Payload<>());
    }

    @Benchmark
    public void matchingObserver() {
        observedEvent.fire(new Observed());
    }

    @Benchmark
    public void selectMatchingObserver() {
        event.select(Observed.class).fire(new Observed());
    }

    @Benchmark
    public void beanManagerOneShot() {
        beanManager.getEvent().select(Signal.class).fire(signal);
    }

    @Benchmark
    public void chainedQualifier() {
        event.select(Signal.class).select(MarkerLiteral.INSTANCE).fire(signal);
    }

    @Benchmark
    public void rotatingTypes(Rotation rotation) {
        rotation.next().fire(genericEvent);
    }

    @State(Scope.Thread)
    public static class Rotation {

        @Param({ "8", "9" })
        public int typeCount;

        private final List<Selection<?>> selections = List.of(
                new Selection<>(new TypeLiteral<Payload<String>>() {}),
                new Selection<>(new TypeLiteral<Payload<Integer>>() {}),
                new Selection<>(new TypeLiteral<Payload<Long>>() {}),
                new Selection<>(new TypeLiteral<Payload<Double>>() {}),
                new Selection<>(new TypeLiteral<Payload<Float>>() {}),
                new Selection<>(new TypeLiteral<Payload<Short>>() {}),
                new Selection<>(new TypeLiteral<Payload<Byte>>() {}),
                new Selection<>(new TypeLiteral<Payload<Character>>() {}),
                new Selection<>(new TypeLiteral<Payload<Boolean>>() {}));
        private int index;

        Selection<?> next() {
            Selection<?> selected = selections.get(index);
            index++;
            if (index == typeCount) {
                index = 0;
            }
            return selected;
        }
    }

    private static class Selection<T> {

        private final TypeLiteral<Payload<T>> type;
        private final Payload<T> payload = new Payload<>();

        Selection(TypeLiteral<Payload<T>> type) {
            this.type = type;
        }

        void fire(Event<Payload<?>> event) {
            event.select(type).fire(payload);
        }
    }

    @Singleton
    @Unremovable
    public static class Emitter {

        @Inject
        Event<Object> event;

        @Inject
        Event<Payload<?>> genericEvent;

        @Inject
        Event<Payload<String>> typedEvent;

        @Inject
        Event<Observed> observedEvent;

        volatile Observed lastObserved;

        void observeMatching(@Observes Observed event) {
            lastObserved = event;
        }

        void observe(@Observes Unrelated event) {
        }
    }

    public static class Payload<T> {
    }

    public static class Signal {
    }

    public static class Observed {
    }

    public static class Unrelated {
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Marker {
    }

    private static class MarkerLiteral extends AnnotationLiteral<Marker> implements Marker {
        static final MarkerLiteral INSTANCE = new MarkerLiteral();
    }
}
