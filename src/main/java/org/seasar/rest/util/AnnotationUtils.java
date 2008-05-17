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
				T annotation = getMethodLevelAnnotation(eachMethod,
						annotationClass);
				if (annotation != null && !isShadowed(eachMethod, results)) {
					results.add(eachMethod);
				}
			}
		}

		return results;
	}

	/**
	 * 指定された型の注釈が存在する場合は、指定された型の要素の注釈を返します。 <br />
	 * 検索対象に全ての継承されたクラス、全ての実装されているインタフェースが含まれます。
	 * 
	 * @param <A>
	 * @param theClass
	 * @param annotationClass
	 * @return
	 * 
	 * @see Class#getAnnotation(Class)
	 */
	public static <A extends Annotation> A getClassLevelAnnotation(
			Class<?> theClass, Class<A> annotationClass) {
		if (null == annotationClass)
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

	/**
	 * 指定された型の注釈が存在する場合は、指定された型の要素の注釈を返します。<br />
	 * 検索対象に全ての実装されたインタフェースを含みます。
	 * 
	 * @param <A>
	 * @param method
	 * @param annotationClass
	 * @return
	 * 
	 * @see Method#getAnnotation(Class)
	 */
	public static <A extends Annotation> A getMethodLevelAnnotation(
			Method method, Class<A> annotationClass) {
		if (null == annotationClass)
			throw new NullPointerException();

		A annotation = (A) method.getAnnotation(annotationClass);

		if (null != annotation)
			return annotation;

		Class<?> declaringClass = method.getDeclaringClass();
		for (Class<?> clazz : getInterfaces(declaringClass)) {
			Method abstractMethod;
			try {
				abstractMethod = clazz.getMethod(method.getName(), method
						.getParameterTypes());
				annotation = (A) abstractMethod.getAnnotation(annotationClass);
				if (null != annotation)
					return annotation;
			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}

	/**
	 * この Method
	 * オブジェクトにより表されたメソッドの仮パラメータの注釈を表す配列の配列を、宣言順に返します。基本となるメソッドがパラメータを含まない場合は、長さゼロの配列を返します。メソッドに
	 * 1
	 * つ以上のパラメータがある場合、注釈を含まないパラメータごとに長さゼロの入れ子の配列を返します。返された配列に含まれる注釈オブジェクトは直列化できます。このメソッドの呼び出し元は、返された配列を自由に変更できます。
	 * この変更は、ほかの呼び出し元に返された配列に影響を及ぼしません。 <br />
	 * 検索対象に継承しているインタフェースを含みます。
	 * 
	 * @param method
	 * @return
	 * 
	 * @see Method#getParameterAnnotations()
	 */
	public static Annotation[][] getMethodLevelParameterAnnotations(
			Method method) {
		if (null == method)
			throw new NullPointerException();

		int numParameters = method.getParameterTypes().length;

		Annotation[][] result = method.getParameterAnnotations();

		// java.lang.reflect.Method#getParameterAnnotationsはnullを戻さないので、null検証しない。
		if (!isArrayOfArraysEmpty(result))
			return result;

		Class<?> declaringClass = method.getDeclaringClass();
		for (Class<?> clazz : getInterfaces(declaringClass)) {
			Method abstractMmethod;
			try {
				abstractMmethod = clazz.getMethod(method.getName(), method
						.getParameterTypes());
				result = abstractMmethod.getParameterAnnotations();
				if (!isArrayOfArraysEmpty(result))
					return result;
			} catch (NoSuchMethodException e) {
			}
		}

		// see java.lang.reflect.Method#getParameterAnnotations
		return new Annotation[numParameters][0];
	}

	/**
	 * クラスに継承されてるクラスおよび実装されているインタフェースをリストで返します。
	 * 
	 * @param theClass
	 * @return 自身を含む継承されているクラスの一意なリスト
	 */
	public static List<Class<?>> getInheritClasses(Class<?> theClass) {
		Set<Class<?>> sets = new LinkedHashSet<Class<?>>();
		Class<?> current = theClass;

		while (current != null && !isObjectClass(current)) {
			sets.add(current);
			sets.addAll(getInterfaces(current));
			current = current.getSuperclass();
		}

		return new ArrayList<Class<?>>(sets);
	}

	/**
	 * java.lang.Objectを除くクラスが継承している全てのクラス(自クラスを含む)を返します。
	 * 
	 * @param theClass
	 * @return all extended classes
	 */
	public static List<Class<?>> getSuperClasses(Class<?> theClass) {
		ArrayList<Class<?>> results = new ArrayList<Class<?>>();
		Class<?> current = theClass;
		while (current != null && !isObjectClass(current)) {
			results.add(current);
			current = current.getSuperclass();
		}
		return results;
	}

	/**
	 * クラスが実装している全てのインタフェースを返します。
	 * 
	 * @param theClass
	 * @return
	 */
	public static List<Class<?>> getInterfaces(Class<?> theClass) {
		Set<Class<?>> sets = new LinkedHashSet<Class<?>>();
		Class<?>[] classes = theClass.getInterfaces();
		for (Class<?> clazz : classes) {
			sets.add(clazz);
			sets.addAll(getInterfaces(clazz));
		}
		return new ArrayList<Class<?>>(sets);
	}

	/**
	 * @see Method#getParameterAnnotations()
	 */
	public static <A extends Annotation> boolean isMethodLevelAnnotationPresent(
			Method method, Class<A> annotationClass) {
		return getMethodLevelAnnotation(method, annotationClass) != null;
	}

	/**
	 * 配列の配列が要素を持つかどうかを検証する。
	 * <p>
	 * 一つでも要素があればTrue、一つもなければfalse
	 * </p>
	 * 
	 * @param arrayOfArrays
	 * @return
	 */
	private static boolean isArrayOfArraysEmpty(Object[][] arrayOfArrays) {
		int items = 0;
		for (int i = 0; i < arrayOfArrays.length; i++) {
			items += arrayOfArrays[i].length;
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

		// TODO もっといい方法があったとおもう・・・
		return theClass.getName().equals("java.lang.Object");
	}
}
