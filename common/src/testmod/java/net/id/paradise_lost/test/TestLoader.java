package net.id.paradise_lost.test;

import net.id.paradise_lost.test.client.ClientTest;
import net.id.paradise_lost.test.server.ServerTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class TestLoader {
    private TestLoader() {
    }

    @SuppressWarnings("unchecked")
    public static List<Test> load(Class<?> type) {
        String defaultGroup = defaultGroup(type);
        List<Test> tests = new ArrayList<>();
        List<Method> methods = Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(ClientTest.class) || m.isAnnotationPresent(ServerTest.class) || m.isAnnotationPresent(TestFactory.class))
                .sorted(Comparator.comparing(Method::getName))
                .toList();
        for (Method method : methods) {
            Object result = invoke(method);
            ClientTest client = method.getAnnotation(ClientTest.class);
            ServerTest server = method.getAnnotation(ServerTest.class);
            if (client == null && server == null) {
                tests.addAll((List<Test>) result);
                continue;
            }
            List<Step> steps = switch (result) {
                case Step step -> List.of(step);
                case List<?> list -> (List<Step>) list;
                default -> throw new IllegalStateException(describe(method) + " must return Step or List<Step>");
            };
            String name = client != null ? client.value() : server.value();
            String group = client != null ? client.group() : server.group();
            tests.add(new Test(group.isEmpty() ? defaultGroup : group, name, steps));
        }
        return tests;
    }

    private static Object invoke(Method method) {
        if (!Modifier.isStatic(method.getModifiers()) || method.getParameterCount() != 0)
            throw new IllegalStateException(describe(method) + " must be static and take no arguments");
        method.setAccessible(true);
        try {
            return method.invoke(null);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("could not build " + describe(method), e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String defaultGroup(Class<?> type) {
        return type.getSimpleName().replaceFirst("Tests$", "").replaceAll("(?<=.)(\\p{Lu})", " $1");
    }

    private static String describe(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }
}
