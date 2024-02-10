package edu.java.bot.reflection;

public class Util {
    private Util() {
    }

    public static void findClassAnnotations(String packageName,
                                            ConsumerBinary consumer) {

        org.reflections.Reflections
                reflections =
                new org.reflections.Reflections(packageName, new org.reflections.scanners.SubTypesScanner(false));

        java.util.Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        for (Class<?> clazz : allClasses) {
            java.lang.annotation.Annotation[] annotations = clazz.getAnnotations();
            for (java.lang.annotation.Annotation annotation : annotations) {
                consumer.accept(annotation, clazz);

            }
        }
    }
}
