package org.seasar.rest.context;

import org.restlet.data.Request;
import org.restlet.data.Response;
import org.seasar.framework.container.ComponentDef;

public class RestletContext {
    private Object resource;
    private ComponentDef componentDef;
    private Request request;
    private Response response;
    private boolean isStateful;
    
    @Deprecated
    private Object componentInstance;
    @Deprecated
    public Object getComponentInstance() {
        // FIXME who's responsible to get/set component?
        if(this.componentInstance == null) {
            this.componentInstance = this.componentDef.getComponent();
        }
        return componentInstance;
    }
    @Deprecated
    public void setComponentInstance(Object componentInstance) {
        this.componentInstance = componentInstance;
    }
    
    public Object getResource() {
        return resource;
    }
    public void setResource(Object resource) {
        this.resource = resource;
    }
    
    public ComponentDef getComponentDef() {
        return componentDef;
    }
    public void setComponentDef(ComponentDef componentDef) {
        this.componentDef = componentDef;
    }
    
    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }
    
    public boolean isStateful() {
        return isStateful;
    }
    public void setStateful(boolean isStateful) {
        this.isStateful = isStateful;
    }
}
