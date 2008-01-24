package org.seasar.rest.method;

import java.lang.reflect.Method;

public interface Activatable {
    void activateWith(Method method);
    boolean isActivated();
}
