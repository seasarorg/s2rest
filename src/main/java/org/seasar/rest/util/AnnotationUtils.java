package org.seasar.rest.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * note: this code is borrowed from JUnit4.4.
 */
public class AnnotationUtils {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(AnnotationUtils.class);

	public static Object getValue(Annotation annotation, String name) {
		Method method = ClassUtil.getMethod(annotation.getClass(), name,
				new Class[] {});
		return MethodUtil.invoke(method, annotation, new Class[] {});
	}

	public static <T extends Annotation> List<Method> getAnnotatedMethods(
			Class<?> theClass, Class<T> annotationClass) {

		List<Method> results = new ArrayList<Method>();

		for (Class<?> eachClass : getSuperClasses(theClass)) {
			Method[] methods = eachClass.getDeclaredMethods();
			for (Method eachMethod : methods) {
				T annotation = getMethodAnnotation(eachMethod, annotationClass);
				if (annotation != null && !isShadowed(eachMethod, results)) {
					results.add(eachMethod);
				}
			}
		}

		return results;
	}

	public static <T extends Annotation> T getMethodAnnotation(Method method,
			Class<T> annotationClass) {
		if (annotationClass == null)
			throw new NullPointerException();

		T annotation = (T) method.getAnnotation(annotationClass);

		if (null != annotation)
			return annotation;

		Class<?> clazz = method.getDeclaringClass();
		for (Class<?> clazzz : getInterfaces(clazz)) {
			Method method1;
			try {
				method1 = clazzz.getMethod(method.getName(), method
						.getParameterTypes());
				annotation = (T) method1.getAnnotation(annotationClass);
				if (null != annotation)
					return annotation;

			} catch (Exception e) {
				// そんなメソッドないよ
			}

		}

		return null;
	}

	public static <A extends Annotation> boolean isAnnotationPresent(
			Method method, Class<A> annotationClass) {
		return getMethodAnnotation(method, annotationClass) != null;
	}

	/**
	 * クラスに指定されたアノテーションを継承を含んで取得する。 検索順は、クラス、スーパークラス、インタフェース
	 * 
	 * @param <A>
	 * @param theClass
	 * @param annotationClass
	 * @return
	 */
	public static <A extends Annotation> A getAnnotation(Class<?> theClass,
			Class<A> annotationClass) {
		if (annotationClass == null)
			throw new NullPointerException();

		A annotation = (A) theClass.getAnnotation(annotationClass);

		if (null != annotation)
			return annotation;

		for (Class<?> clazz : getInheritClasses(theClass)) {
			annotation = (A) clazz.getAnnotation(annotationClass);
			if (null != annotation)
				return annotation;
		}

		return null;
	}

	public static List<Class<?>> getInheritClasses(Class<?> theClass) {
		Set<Class<?>> set = new LinkedHashSet<Class<?>>();
		Class<?> current = theClass;

		while (current != null && !isObjectClass(current)) {
			set.add(current);
			set.addAll(getInterfaces(current));
			current = current.getSuperclass();
		}

		ArrayList<Class<?>> results = new ArrayList<Class<?>>();
		for (Class<?> clazz : set) {
			results.add(clazz);
		}

		return results;
	}

	public static List<Class<?>> getSuperClasses(Class<?> theClass) {
		ArrayList<Class<?>> results = new ArrayList<Class<?>>();
		Class<?> current = theClass;
		while (current != null && !isObjectClass(current)) {
			results.add(current);
			current = current.getSuperclass();
		}
		return results;
	}

	public static List<Class<?>> getInterfaces(Class<?> theClass) {
		ArrayList<Class<?>> results = new ArrayList<Class<?>>();
		Class<?>[] classes = theClass.getInterfaces();
		for (Class<?> clazz : classes) {
			results.add(clazz);
			results.addAll(getInterfaces(clazz));
		}
		return results;
	}

	public static Annotation[][] getParameterAnnotations(Method method) {
		int numParameters = method.getParameterTypes().length;

		Annotation[][] result = method.getParameterAnnotations();

		if (!isEmpty(result))
			return result;

		Class<?> clazz = method.getDeclaringClass();
		for (Class<?> clazzz : getInterfaces(clazz)) {
			Method method1;
			try {
				method1 = clazzz.getMethod(method.getName(), method
						.getParameterTypes());
				result = method1.getParameterAnnotations();

				if (!isEmpty(result))
					return result;

			} catch (Exception e) {
				// そんなメソッドないよ
			}

		}

		return new Annotation[numParameters][0];

	}

	// TODO FIXME
	private static boolean isEmpty(Object[][] matrix) {
		int items = 0;
		for (int i = 0; i < matrix.length; i++) {
			items += matrix[i].length;
		}
		return (0 == items);
	}

	private static boolean isShadowed(Method method, List<Method> results) {
		for (Method each : results) {
			if (isShadowed(method, each))
				return true;
		}
		return false;
	}

	private static boolean isShadowed(Method current, Method previous) {
		if (!previous.getName().equals(current.getName()))
			return false;
		if (previous.getParameterTypes().length != current.getParameterTypes().length)
			return false;
		for (int i = 0; i < previous.getParameterTypes().length; i++) {
			if (!previous.getParameterTypes()[i].equals(current
					.getParameterTypes()[i]))
				return false;
		}
		return true;
	}

	private static boolean isObjectClass(Class<?> theClass) {
		if (null == theClass)
			throw new NullPointerException();

		return theClass.getName().equals("java.lang.Object");
	}
}
