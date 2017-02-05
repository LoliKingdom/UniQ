package xyz.domi1819.uniq;

public class Reflect
{
    public static Class getNestedClass(Class mainClass, String simpleName)
    {
        Class[] classes = mainClass.getDeclaredClasses();

        for (Class clazz : classes)
        {
            if (clazz.getSimpleName().equals(simpleName))
            {
                return clazz;
            }
        }

        return null;
    }
}
