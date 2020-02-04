package io.quarkus.arc.benchmarks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import io.quarkus.arc.benchmarks.ReflectionsBenchmark.Baz;
import io.quarkus.arc.benchmarks.ReflectionsBenchmark.Foo;
import io.quarkus.arc.impl.Reflections;

public class ReflectionsTest {

    @Test
    public void testFindField() {
        Field field = Reflections.findField(Foo.class, "name");
        assertNotNull(field);
        assertEquals(Baz.class, field.getDeclaringClass());
    }

    @Test
    public void testFindMethod() {
        Method method = Reflections.findMethod(Foo.class, "getName");
        assertNotNull(method);
        assertEquals(Baz.class, method.getDeclaringClass());
    }

}
