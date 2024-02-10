package edu.java.bot.reflection;

import edu.java.bot.annotation.CurrentPrinter;
import edu.java.bot.reflection.test.Test1;
import edu.java.bot.reflection.test.Test2;
import edu.java.bot.reflection.test.Test3;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    @DisplayName("find Class Annotations should Find Classes And Apply Action With Their Annotations")
    public void findClassAnnotations_shouldFindClassesAndApplyActionWithTheirAnnotations() {
        List<ClassAnnotation> expected = Stream.of(new ClassAnnotation(Test2.class, CurrentPrinter.class),new ClassAnnotation(Test2.class, Deprecated.class), new ClassAnnotation(
                Test3.class, Deprecated.class) ).sorted().toList();
        List<ClassAnnotation> actual = new ArrayList<>();
        Util.findClassAnnotations("edu/java/bot/reflection/test", ((annotation, clazz) -> {
            actual.add(new ClassAnnotation(clazz, annotation.annotationType()));
        }));
        assertArrayEquals(expected.toArray(), actual.stream().sorted().toList().toArray());
    }
    @Data
    private class ClassAnnotation implements Comparable<ClassAnnotation>{
        private final Class aClass;
        private final Class annotation;

        @Override
        public int compareTo(@NotNull UtilTest.ClassAnnotation classAnnotation) {
            int classNameComparison = aClass.getName().compareTo(classAnnotation.aClass.getName());
            if (classNameComparison == 0) {
                return annotation.getName().compareTo(classAnnotation.annotation.getName());
            }

            return classNameComparison;
        }

    }
}
