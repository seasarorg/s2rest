package org.seasar.rest.uri.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import org.restlet.data.Request;
import org.seasar.framework.util.MethodUtil;
import org.seasar.rest.uri.RequestToPropertyMapper;
import org.seasar.rest.util.AnnotationUtils;

public class Jsr311RequestToPropertyMapper implements RequestToPropertyMapper {
    
    public void mapAll(Request request, Object obj, Class<?> clazz) {
        Map<String, Object> attributes = request.getAttributes();
        
        //TODO QueryParam,MatrixParam,HeaderParam
        List<Method> methods = AnnotationUtils.getAnnotatedMethods(clazz, PathParam.class);
        
        for (Method method : methods) {
            PathParam uriParam = method.getAnnotation(PathParam.class);
            String uriParamName = uriParam.value();
            if(attributes.containsKey(uriParamName)) {
                Object uriValue = attributes.get(uriParamName);
                MethodUtil.invoke(method, obj, new Object[]{uriValue});
            }
        }
    }
}