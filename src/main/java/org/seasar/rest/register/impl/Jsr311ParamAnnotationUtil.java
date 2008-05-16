package org.seasar.rest.register.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.ws.rs.PathParam;

import org.restlet.util.Variable;
import org.seasar.framework.log.Logger;

public class Jsr311ParamAnnotationUtil {

	@SuppressWarnings("unused")
	private static final Logger log = Logger
			.getLogger(Jsr311ParamAnnotationUtil.class);

	public static void collectVariableHintFor(Method method,
			Map<String, Variable> variables) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> paramType = parameterTypes[i];
			Annotation[] annotations = parameterAnnotations[i];
			inspectParam(variables, annotations, paramType);
		}
	}

	private static void inspectParam(Map<String, Variable> variables,
			Annotation[] annotations, Class<?> paramType) {
		for (Annotation ann : annotations) {
			if (ann instanceof PathParam) {
				PathParam uriParam = (PathParam) ann;
				if (isDigitClass(paramType)) {
					Variable variable = new Variable(Variable.TYPE_DIGIT);
					variables.put(uriParam.value(), variable);
				}
			}
			// TODO QueryParam,MatrixParam,HeaderParam
		}
	}

	private static boolean isDigitClass(Class<?> type) {
		if (type.isAssignableFrom(Number.class)) {
			return true;
		} else if (type == int.class) {
			return true;
		} else if (type == long.class) {
			return true;
		} else if (type == short.class) {
			return true;
		} else if (type == long.class) {
			return true;
		} else if (type == double.class) {
			return true;
		} else if (type == float.class) {
			return true;
		}
		return false;
	}
}
