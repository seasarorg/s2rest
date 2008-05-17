package org.seasar.rest.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Test;
import org.seasar.framework.util.ClassUtil;

public class AnnotationUtilsTest {

	@Test
	public void getValue() throws Exception {
		Method method = ClassUtil.getMethod(SampleClass.class, "hoge",
				new Class[] {});
		Annotation ann = method.getAnnotation(Path.class);

		assertThat(AnnotationUtils.getValue(ann, "value"),
				is((Object) "foo/bar"));
	}

	@Test
	public void testGetInterfaces() throws Exception {
		// Implemented Hoge
		List<Class<?>> list = AnnotationUtils.getInterfaces(SampleClass1.class);
		assertEquals(list.size(), 1);
		assertEquals(list.get(0), Hoge.class);

		// Implemented Huga. Huga extended Hoge
		list = AnnotationUtils.getInterfaces(SampleClass2.class);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), Huga.class);
		assertEquals(list.get(1), Hoge.class);

		// Implemented Hoge & Huga
		list = AnnotationUtils.getInterfaces(SampleClass3.class);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), Hoge.class);
		assertEquals(list.get(1), Huga.class);

	}

	@Test
	public void testGetSuperClasses() throws Exception {
		// extends SampleClass
		// return not exists java.lang.Object
		List<Class<?>> list = AnnotationUtils
				.getSuperClasses(SampleClass4.class);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), SampleClass4.class);
		assertEquals(list.get(1), SampleClass.class);

		// extends SampleClass4
		// return not exists java.lang.Object
		list = AnnotationUtils.getSuperClasses(SampleClass5.class);
		assertEquals(list.size(), 3);
		assertEquals(list.get(0), SampleClass5.class);
		assertEquals(list.get(1), SampleClass4.class);
		assertEquals(list.get(2), SampleClass.class);

	}

	@Test
	public void testGetMethodAnnotation() throws Exception {
		// SampleClass1#hogehoge has Path Annotation
		Method method = ClassUtil.getMethod(SampleClass1.class, "hogehoge",
				new Class[] {});
		Path path = AnnotationUtils
				.getMethodLevelAnnotation(method, Path.class);
		assertNotNull(path);

		// SampleClass2#hogehoge doesn't have Path Annotation
		// Hoge#hogehoge has Path Annotation
		method = ClassUtil.getMethod(SampleClass2.class, "hogehoge",
				new Class[] {});
		path = AnnotationUtils.getMethodLevelAnnotation(method, Path.class);
		assertNotNull(path);

		// Hoge#hogehoge has Path Annotation
		method = ClassUtil.getMethod(SampleClass3.class, "hogehoge",
				new Class[] {});
		path = AnnotationUtils.getMethodLevelAnnotation(method, Path.class);
		assertNotNull(path);

		// anyone have Path Annotation
		method = ClassUtil.getMethod(SampleClass3.class, "hugahuga",
				new Class[] {});
		path = AnnotationUtils.getMethodLevelAnnotation(method, Path.class);
		assertNull(path);

		// Annotation class is Null
		try {
			path = AnnotationUtils.getMethodLevelAnnotation(method, null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testGetMethodParameterAnnotation() throws Exception {
		// SampleClass1#getLongId has PathParam Annotation
		Method method = ClassUtil.getMethod(SampleClass1.class, "getLongId",
				new Class[] { Long.class });
		Annotation[][] aa = AnnotationUtils
				.getMethodLevelParameterAnnotations(method);
		assertEquals(aa.length, 1);
		assertEquals(aa[0].length, 1);

		// Hoge#getLongId has PathParam Annotation
		method = ClassUtil.getMethod(SampleClass2.class, "getLongId",
				new Class[] { Long.class });
		aa = AnnotationUtils.getMethodLevelParameterAnnotations(method);
		assertEquals(1, aa.length);
		assertEquals(1, aa[0].length);

		// anyone have Parameter Annotation
		method = ClassUtil.getMethod(SampleClass2.class, "hugahuga",
				new Class[] {});
		aa = AnnotationUtils.getMethodLevelParameterAnnotations(method);
		assertEquals(0, aa.length);

		// Method is Null
		try {
			aa = AnnotationUtils.getMethodLevelParameterAnnotations(null);
			fail();
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testGetClassAnnotation() throws Exception {

		// Class has Path Annotation
		Path path = AnnotationUtils.getClassLevelAnnotation(SampleClass.class,
				Path.class);
		assertNotNull(path);
		assertEquals("foo/bar", AnnotationUtils.getValue(path, "value"));

		GET get = AnnotationUtils.getClassLevelAnnotation(SampleClass.class,
				GET.class);
		assertNull(get);

		// Interface has Path Annotation
		path = AnnotationUtils.getClassLevelAnnotation(SampleClass3.class,
				Path.class);
		assertNotNull(path);
		assertEquals("Huga", AnnotationUtils.getValue(path, "value"));

		get = AnnotationUtils.getClassLevelAnnotation(SampleClass3.class,
				GET.class);
		assertNull(get);

		// Class and Interface have Path Annotation
		path = AnnotationUtils.getClassLevelAnnotation(SampleClass2.class,
				Path.class);
		assertNotNull(path);
		assertEquals("hugahuga", AnnotationUtils.getValue(path, "value"));

		// SuperClass has Path Annotation
		path = AnnotationUtils.getClassLevelAnnotation(SampleClass4.class,
				Path.class);
		assertNotNull(path);
		assertEquals("foo/bar", AnnotationUtils.getValue(path, "value"));

		// Class, Interface, SuperClass has Path Annotation
		path = AnnotationUtils.getClassLevelAnnotation(SampleClass5.class,
				Path.class);
		assertNotNull(path);
		assertEquals("sample5", AnnotationUtils.getValue(path, "value"));

	}

	// mock
	public static interface Hoge {
		@Path("foo/bar")
		void hogehoge();

		Long getLongId(@PathParam("id")
		Long x);
	}

	@Path("Huga")
	public static interface Huga extends Hoge {
		void hugahuga();
	}

	@Path("foo/bar")
	public static class SampleClass {
		@Path("foo/bar")
		public void hoge() {
		}
	}

	public static class SampleClass1 implements Hoge {

		@Path("foo/barbar")
		public void hogehoge() {
		}

		public Long getLongId(Long x) {
			return null;
		}
	}

	@Path("hugahuga")
	public static class SampleClass2 implements Huga {

		public void hogehoge() {
		}

		public void hugahuga() {
		}

		public Long getLongId(Long x) {
			return null;
		}

	}

	public static class SampleClass3 implements Hoge, Huga {

		public void hogehoge() {
		}

		public void hugahuga() {
		}

		public Long getLongId(Long x) {
			return null;
		}

	}

	public static class SampleClass4 extends SampleClass {

	}

	@Path("sample5")
	public static class SampleClass5 extends SampleClass4 {

	}

}
