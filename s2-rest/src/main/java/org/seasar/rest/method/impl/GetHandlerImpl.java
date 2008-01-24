package org.seasar.rest.method.impl;

import java.lang.reflect.Method;

import org.restlet.data.Request;
import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.util.MethodUtil;
import org.seasar.rest.RestRuntimeException;
import org.seasar.rest.context.RestletContext;
import org.seasar.rest.method.GetHandler;
import org.seasar.rest.uri.RequestToPropertyMapper;
import org.seasar.rest.uri.UriParser;


public class GetHandlerImpl implements GetHandler {
    private Method method;
    private ComponentInvoker componentInvoker;
    private UriParser uriParser;
    private RestletContext restletContext;
    private RequestToPropertyMapper propertyMapper;
    
    public void setComponentInvoker(ComponentInvoker componentInvoker) {
        this.componentInvoker = componentInvoker;
    }
    public void setUriParser(UriParser uriParser) {
        this.uriParser = uriParser;
    }
    public void setRestletContext(RestletContext restletContext) {
        this.restletContext = restletContext;
    }
    public void setRequestToPropertyMapper(RequestToPropertyMapper propertyMapper) {
        this.propertyMapper = propertyMapper;
    }
    
    
    public void handle() {
        ComponentDef componentDef = this.restletContext.getComponentDef();
        Request request = this.restletContext.getRequest();
        
        Object[] args = this.uriParser.toMethodArgs(request, componentDef, this.method);
        Object resource = null;
        
        if(restletContext.isStateful()) {
            //XXX deprecated since JSR311 0.6
            Object componentInstance = this.restletContext.getComponentInstance();
            propertyMapper.mapAll(request, componentInstance, componentDef.getComponentClass());
            resource = MethodUtil.invoke(this.method, componentInstance, args);
        } else {
            try {
                resource = this.componentInvoker.invoke(componentDef.getComponentName(), method.getName(), args);
            } catch (Throwable e) {
                throw new RestRuntimeException(e);
            }
        }
        
        this.restletContext.setResource(resource);
    }
    
    public void activateWith(Method method) {
        this.method = method;
    }
    public boolean isActivated() {
        return this.method != null;
    }

}
