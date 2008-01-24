package org.seasar.rest.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * note: this code is borrowed from JUnit4.4.
 */
public class AnnotationUtils {

    public static Object getValue(Annotation annotation, String name) {
        Method method = ClassUtil.getMethod(annotation.getClass(), name, new Class[]{});
        return MethodUtil.invoke(method, annotation, new Class[]{});
    }
    
    public static List<Method> getAnnotatedMethods(Class<?> theClass, Class<? extends Annotation> annotationClass) {
        List<Method> results= new ArrayList<Method>();
        for (Class<?> eachClass : getSuperClasses(theClass)) {
            Method[] methods= eachClass.getDeclaredMethods();
            for (Method eachMethod : methods) {
                Annotation annotation= eachMethod.getAnnotation(annotationClass);
                if (annotation != null && ! isShadowed(eachMethod, results)) 
                    results.add(eachMethod);
            }
        }
        return results;
    }
    
    public static List<Class<?>> getSuperClasses(Class< ?> theClass) {
        ArrayList<Class<?>> results= new ArrayList<Class<?>>();
        Class<?> current= theClass;
        while (current != null) {
            results.add(current);
            current= current.getSuperclass();
        }
        return results;
    }
    
    private static boolean isShadowed(Method method, List<Method> results) {
        for (Method each : results) {
            if (isShadowed(method, each))
                return true;
        }
        return false;
    }
    
    private static boolean isShadowed(Method current, Method previous) {
        if (! previous.getName().equals(current.getName()))
            return false;
        if (previous.getParameterTypes().length != current.getParameterTypes().length)
            return false;
        for (int i= 0; i < previous.getParameterTypes().length; i++) {
            if (! previous.getParameterTypes()[i].equals(current.getParameterTypes()[i]))
                return false;
        }
        return true;
    }
}
