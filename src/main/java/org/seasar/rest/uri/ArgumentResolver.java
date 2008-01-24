package org.seasar.rest.uri;

import java.lang.reflect.Method;

public interface ArgumentResolver {
    String[] argumentNamesFor(Method method);
}