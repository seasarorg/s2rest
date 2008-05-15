package org.seasar.rest.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.ws.rs.Path;

import org.junit.Test;
import org.seasar.framework.util.ClassUtil;

public class AnnotationUtilsTest {

	public static class SampleClass {
		@Path("foo/bar")
		public void hoge() {
		}
	}

	@Test
	public void getValue() throws Exception {
		Method method = ClassUtil.getMethod(SampleClass.class, "hoge",
				new Class[] {});
		Annotation ann = method.getAnnotation(Path.class);

		assertThat(AnnotationUtils.getValue(ann, "value"),
				is((Object) "foo/bar"));
	}

}
