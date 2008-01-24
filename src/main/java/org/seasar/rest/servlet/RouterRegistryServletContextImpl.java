package org.seasar.rest.servlet;

import javax.servlet.ServletContext;

import org.restlet.Application;
import org.restlet.Router;
import org.seasar.rest.register.RouterRegistry;

public class RouterRegistryServletContextImpl implements RouterRegistry {
    private static final String DEFAULT_RESTLET_APPLICATION_KEY =
        "com.noelios.restlet.ext.servlet.ServerServlet.application";
    
    private String applicationKey = DEFAULT_RESTLET_APPLICATION_KEY;
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
    
    private ServletContext servletContext;
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public Router getRouter() {
        Application app = (Application) this.servletContext.getAttribute(this.applicationKey);
        if(app == null) {
           throw new IllegalStateException("Application is not binded to ServletContext");
        }
        return (Router) app.getRoot();
    }
}
