package org.seasar.rest.uri.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.Request;
import org.seasar.framework.container.ComponentDef;
import org.seasar.rest.uri.ArgumentResolver;
import org.seasar.rest.uri.UriParser;

public class UriParserImpl implements UriParser {
    private ArgumentResolver argumentResolver;

    public void setArgumentResolver(ArgumentResolver argumentResolver) {
        this.argumentResolver = argumentResolver;
    }

    public Object[] toMethodArgs(Request request, ComponentDef componentDef, Method method) {
        String[] argNames = this.argumentResolver.argumentNamesFor(method);
        
        Map<String, Object> attributes = request.getAttributes();
        List<Object> argList = new ArrayList<Object>();
        for (String argName : argNames) {
            argList.add(attributes.get(argName));
        }
        
        // FIXME argList.size validation
        
        Object[] args = (Object[]) argList.toArray(new Object[argList.size()]);
//        Class<?>[] parameterTypes = method.getParameterTypes();
        
        return args;
    }

}
