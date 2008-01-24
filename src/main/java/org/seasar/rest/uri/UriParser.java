package org.seasar.rest.uri;

import java.lang.reflect.Method;

import org.restlet.data.Request;
import org.seasar.framework.container.ComponentDef;

public interface UriParser {
    Object[] toMethodArgs(Request request, ComponentDef componentDef, Method method);
}