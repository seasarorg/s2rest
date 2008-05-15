package org.seasar.rest.uri.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;

import org.seasar.rest.uri.ArgumentResolver;
import org.seasar.rest.util.AnnotationUtils;

public class Jsr311UriParamResolver implements ArgumentResolver {

	public String[] argumentNamesFor(Method method) {
		List<String> names = new ArrayList<String>();
		Annotation[][] parameterAnnotations = AnnotationUtils
				.getParameterAnnotations(method);
		for (Annotation[] annotations : parameterAnnotations) {
			inspectParam(names, annotations);
		}
		return (String[]) names.toArray(new String[names.size()]);
	}

	private void inspectParam(List<String> names, Annotation[] annotations) {
		for (Annotation ann : annotations) {
			if (ann instanceof PathParam) {
				PathParam uriParam = (PathParam) ann;
				names.add(uriParam.value());
			}
			// TODO QueryParam,MatrixParam,HeaderParam
		}
	}
}
