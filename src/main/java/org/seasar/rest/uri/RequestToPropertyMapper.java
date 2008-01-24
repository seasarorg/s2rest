package org.seasar.rest.uri;

import org.restlet.data.Request;

public interface RequestToPropertyMapper {
    void mapAll(Request request, Object obj, Class<?> clazz);
}