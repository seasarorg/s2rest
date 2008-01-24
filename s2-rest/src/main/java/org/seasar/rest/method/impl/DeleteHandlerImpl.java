package org.seasar.rest.method.impl;

import java.lang.reflect.Method;

import org.restlet.data.Response;
import org.restlet.data.Status;
import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.container.ComponentDef;
import org.seasar.rest.RestRuntimeException;
import org.seasar.rest.context.RestletContext;
import org.seasar.rest.method.DeleteHandler;

public class DeleteHandlerImpl implements DeleteHandler {
    private Method method;
    private ComponentInvoker componentInvoker;
    private RestletContext restletContext;
    
    public void setComponentInvoker(ComponentInvoker componentInvoker) {
        this.componentInvoker = componentInvoker;
    }
    public void setRestletContext(RestletContext restletContext) {
        this.restletContext = restletContext;
    }
    
    
    public void handle() {
        Object resource = this.restletContext.getResource();
        ComponentDef componentDef = this.restletContext.getComponentDef();
        Response response = this.restletContext.getResponse();
        
        if(resource == null) {
            throw new RestRuntimeException("DELETE target is null");
        }
        
        try {
            this.componentInvoker.invoke(componentDef.getComponentName(), method.getName(), new Object[]{resource});
        } catch (Throwable e) {
            throw new RestRuntimeException(e);
        }
        
        response.setStatus(Status.SUCCESS_NO_CONTENT);
    }
    
    public void activateWith(Method method) {
        this.method = method;
    }
    public boolean isActivated() {
        return this.method != null;
    }
}
