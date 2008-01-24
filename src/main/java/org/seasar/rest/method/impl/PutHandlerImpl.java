package org.seasar.rest.method.impl;

import java.lang.reflect.Method;

import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.container.ComponentDef;
import org.seasar.rest.NoncompliantResourceException;
import org.seasar.rest.RestRuntimeException;
import org.seasar.rest.context.RestletContext;
import org.seasar.rest.method.PutHandler;
import org.seasar.rest.representation.SeasarRepresentation;

public class PutHandlerImpl implements PutHandler {
    private Method method;
    private ComponentInvoker componentInvoker;
    private SeasarRepresentation seasarRepresentation;
    private RestletContext restletContext;
    
    public void setComponentInvoker(ComponentInvoker componentInvoker) {
        this.componentInvoker = componentInvoker;
    }
    public void setSeasarRepresentation(SeasarRepresentation seasarRepresentation) {
        this.seasarRepresentation = seasarRepresentation;
    }
    public void setRestletContext(RestletContext restletContext) {
        this.restletContext = restletContext;
    }


    public void handle(Representation inComingRepresentation) {
        ComponentDef componentDef = this.restletContext.getComponentDef();
        Response response = this.restletContext.getResponse();
        
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        if(parameterTypes == null || parameterTypes.length != 1) {
            throw new NoncompliantResourceException("put method should have one argument");
        }
        Object obj = seasarRepresentation.inComing(inComingRepresentation, parameterTypes[0]);
        
        try {
            this.componentInvoker.invoke(componentDef.getComponentName(), method.getName(), new Object[]{obj});
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
