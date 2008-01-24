package org.seasar.rest.resource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import org.restlet.Context;
import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;
import org.seasar.rest.MethodType;
import org.seasar.rest.context.RestletContext;


public class SeasarResourceFinder extends Finder {
    private static final Logger log = Logger.getLogger(SeasarResourceFinder.class);
    
    private final ComponentDef componentDef;
    private Map<MethodType, Method> methodMap = new HashMap<MethodType, Method>();
    private List<String> produceMimes = new ArrayList<String>();
    private List<String> consumeMimes = new ArrayList<String>();
    private boolean isStateful = false;
    
    public SeasarResourceFinder(Context context, ComponentDef componentDef) {
        super(context);
        this.componentDef = componentDef;
    }

    @Override
    public Resource findTarget(Request request, Response response) {
        log.debug("START  FINDING RESOURCE");
        
        RestletContext restletContext = SingletonS2Container.getComponent(RestletContext.class);
        restletContext.setComponentDef(this.componentDef);
        restletContext.setRequest(request);
        restletContext.setResponse(response);
        restletContext.setStateful(this.isStateful);
        
        SeasarResource resource = SingletonS2Container.getComponent(SeasarResource.class);
        for (String mimeName : this.produceMimes) {
            resource.addVariant(mimeName);
        }
        for (String mimeName : this.consumeMimes) {
            resource.addVariant(mimeName);
        }
        resource.init(getContext(), request, response);
        
        if(methodMap.containsKey(MethodType.GET)) {
            resource.activateGet(methodMap.get(MethodType.GET));
        }
        if(methodMap.containsKey(MethodType.POST)) {
            resource.activatePost(methodMap.get(MethodType.POST));
        }
        if(methodMap.containsKey(MethodType.PUT)) {
            resource.activatePut(methodMap.get(MethodType.PUT));
        }
        if(methodMap.containsKey(MethodType.DELETE)) {
            resource.activateDelete(methodMap.get(MethodType.DELETE));
        }
        
        if(!(org.restlet.data.Method.POST.equals(request.getMethod()))) {
            resource.locate();
        }
        
        log.debug("FINISH FINDING RESOURCE");
        return resource;
    }
    
    public Collection<Method> methods() {
        return this.methodMap.values();
    }
    
    public void addHandler(Method method, String httpMethod) {
        if(HttpMethod.GET.equalsIgnoreCase(httpMethod)) {
            methodMap.put(MethodType.GET, method);
        } else if(HttpMethod.POST.equalsIgnoreCase(httpMethod)) {
            methodMap.put(MethodType.POST, method);
        } else if(HttpMethod.PUT.equalsIgnoreCase(httpMethod)) {
            methodMap.put(MethodType.PUT, method);
        } else if(HttpMethod.DELETE.equalsIgnoreCase(httpMethod)) {
            methodMap.put(MethodType.DELETE, method);
        }
    }
    
    public void addProduceMime(String mimeType) {
        if(!(produceMimes.contains(mimeType))) {
            produceMimes.add(mimeType);
        }
    }
    public boolean isProduceMime() {
        return !produceMimes.isEmpty();
    }

    public void addConsumeMime(String mimeType) {
        if(!(consumeMimes.contains(mimeType))) {
            consumeMimes.add(mimeType);
        }
    }
    public boolean isConsumeMime() {
        return !consumeMimes.isEmpty();
    }

    public void setStateful(boolean isStateful) {
        this.isStateful = isStateful;
    }
    
}
