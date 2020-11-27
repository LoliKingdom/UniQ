package re.domi.uniq.utils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    private static Field modifiersField;

    private ReflectionUtils() { }

    @Nullable
    public static Field setAccessible(Class<?> instanceClass, String fieldName) {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field stripFinalField(Field field) {
        if (modifiersField == null) {
            modifiersField = setAccessible(Field.class, ("modifiers"));
        }
        try {
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }

    public static <T, V> void setInstanceFinalField(Class<?> instanceClass, String fieldName, @Nullable T instance, V value) {
        Field field = setAccessible(instanceClass, fieldName);
        stripFinalField(field);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
