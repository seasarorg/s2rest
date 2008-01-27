package org.seasar.rest.register.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.PathParam;

import org.restlet.Route;
import org.restlet.Router;
import org.restlet.util.Template;
import org.restlet.util.Variable;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.rest.register.Jsr311ComponentRegister;
import org.seasar.rest.register.RouterRegistry;
import org.seasar.rest.resource.SeasarResourceFinder;
import org.seasar.rest.util.AnnotationUtils;


public class Jsr311ComponentRegisterImpl implements Jsr311ComponentRegister {
    private static final Logger log = Logger.getLogger(Jsr311ComponentRegisterImpl.class);
    private RouterRegistry routerRegistry;
    
    public void setRouterRegistry(RouterRegistry routerRegistry) {
        this.routerRegistry = routerRegistry;
    }

    public void register(ComponentDef componentDef) {
        Map<String, SeasarResourceFinder> uriMapping = new HashMap<String, SeasarResourceFinder>();
        
        Class<?> componentClass = componentDef.getComponentClass();
        String classUriPattern = resolveClassUriPatternFrom(componentClass);
        introduce(componentDef, uriMapping, classUriPattern);
        
        attachToRouter(uriMapping, componentClass);
    }

    private void attachToRouter(Map<String, SeasarResourceFinder> uriMapping,
            Class<?> componentClass) {
        Router router = this.routerRegistry.getRouter();
        for (Iterator<String> iter = uriMapping.keySet().iterator(); iter.hasNext();) {
            String uriPattern = iter.next();
            SeasarResourceFinder finder = uriMapping.get(uriPattern);
            
            classLevelProduceMime(componentClass, finder);
            classLevelConsumeMime(componentClass, finder);
            
            // TODO put access Filter implementation here
            Route route = router.attach(uriPattern, finder);
            
            registerVariableHintTo(route, finder);
        }
    }

    private String resolveClassUriPatternFrom(Class<?> componentClass) {
        Path classUriTemplate = componentClass.getAnnotation(Path.class);
        String classUriPattern = null;
        if(classUriTemplate != null) {
            classUriPattern = "/" + classUriTemplate.value();
            log.info("classUriPattern is [" + classUriPattern + "]");
        } else {
            classUriPattern = "";
        }
        return classUriPattern;
    }

    private void registerVariableHintTo(Route route, SeasarResourceFinder finder) {
        Template template = route.getTemplate();
        Map<String, Variable> variables = template.getVariables();
        for (Method method : finder.methods()) {
            Jsr311ParamAnnotationUtil.collectVariableHintFor(method, variables);
        }
    }

    private void introduce(ComponentDef componentDef, Map<String, SeasarResourceFinder> uriMapping, String classUriPattern) {
        List<Method> methods = AnnotationUtils.getAnnotatedMethods(componentDef.getComponentClass(), PathParam.class);
        if(methods.isEmpty()) {
            log.debug("resource is stateless");
            inspectEachHttpMethod(componentDef, uriMapping, classUriPattern, false, GET.class);
            inspectEachHttpMethod(componentDef, uriMapping, classUriPattern, false, POST.class);
            inspectEachHttpMethod(componentDef, uriMapping, classUriPattern, false, PUT.class);
            inspectEachHttpMethod(componentDef, uriMapping, classUriPattern, false, DELETE.class);
        } else {
            throw new UnsupportedOperationException("stateful resource is not supported since JSR311 version 0.6");
//            log.info("resource is stateful");
//            inspectEachHttpMethod(componentDef, uriMapping, classUriPattern, true, HttpMethod.class);
        }
    }

    private void inspectEachHttpMethod(ComponentDef componentDef, Map<String, SeasarResourceFinder> uriMapping, String classUriPattern, boolean isStateful, Class<? extends Annotation> httpAnnotationClass) {
        List<Method> methods = AnnotationUtils.getAnnotatedMethods(componentDef.getComponentClass(), httpAnnotationClass);
        for (Method method : methods) {
            inspectHttpMethod(method, componentDef, uriMapping, classUriPattern, isStateful);
        }
    }

    private void inspectHttpMethod(Method method, ComponentDef componentDef, Map<String, SeasarResourceFinder> uriMapping, String classUriPattern, boolean isStateful) {
        String httpMethodStr = getHttpMethodStringFrom(method);
        Path methodUriTemplate = method.getAnnotation(Path.class);
        String uriPattern = null;
        if(methodUriTemplate != null) {
            uriPattern = classUriPattern + "/" + methodUriTemplate.value();
        } else {
            if(StringUtil.isBlank(classUriPattern)) {
                // FIXME which exception should I throw here?
                throw new IllegalStateException("no Path available for "+ ClassUtil.getShortClassName(componentDef.getComponentClass()) + "." + method.getName());
            }
            uriPattern = classUriPattern;
        }
        
        SeasarResourceFinder finder = getFinderFrom(uriMapping, uriPattern, componentDef);
        finder.addHandler(method, httpMethodStr);
        finder.setStateful(isStateful);
        
        methodLevelProduceMime(method, finder);
        methodLevelConsumeMime(method, finder);
        
        log.info("attach [" + ClassUtil.getShortClassName(componentDef.getComponentClass()) + "." + method.getName() + "] to " + httpMethodStr + ":[" + uriPattern + "]");
    }

    private void methodLevelProduceMime(Method method, SeasarResourceFinder finder) {
        ProduceMime produceMime = method.getAnnotation(ProduceMime.class);
        if(produceMime != null) {
            String[] mimeTypes = produceMime.value();
            for (String mimeType : mimeTypes) {
                finder.addProduceMime(mimeType);
            }
        }
    }

    private void methodLevelConsumeMime(Method method, SeasarResourceFinder finder) {
        ConsumeMime consumeMime = method.getAnnotation(ConsumeMime.class);
        if(consumeMime != null) {
            String[] mimeTypes = consumeMime.value();
            for (String mimeType : mimeTypes) {
                finder.addConsumeMime(mimeType);
            }
        }
    }

    private void classLevelProduceMime(Class<?> componentClass, SeasarResourceFinder finder) {
        if(!(finder.isProduceMime())) {
            ProduceMime produceMime = componentClass.getAnnotation(ProduceMime.class);
            if(produceMime != null) {
                for(String mimeType : produceMime.value()) {
                    finder.addProduceMime(mimeType);
                }
            }
        }
    }

    private void classLevelConsumeMime(Class<?> componentClass, SeasarResourceFinder finder) {
        if(!(finder.isConsumeMime())) {
            ConsumeMime consumeMime = componentClass.getAnnotation(ConsumeMime.class);
            if(consumeMime != null) {
                for(String mimeType : consumeMime.value()) {
                    finder.addConsumeMime(mimeType);
                }
            }
        }
    }
    
    private String getHttpMethodStringFrom(Method method) {
        //XXX what happens if multiple annotations exist for one method?
        
        if(method.isAnnotationPresent(GET.class)) {
            return "GET";
        }
        if(method.isAnnotationPresent(POST.class)) {
            return "POST";
        }
        if(method.isAnnotationPresent(PUT.class)) {
            return "PUT";
        }
        if(method.isAnnotationPresent(DELETE.class)) {
            return "DELETE";
        }
        throw new IllegalStateException("Http annotation is not specified. If not specified, the name of the annotated method must begin with one of the method constants(in lowercase). method:["+ ClassUtil.getShortClassName(method.getDeclaringClass()) + "." + method.getName() + "]");
    }
    
    private SeasarResourceFinder getFinderFrom(Map<String, SeasarResourceFinder> uriMapping, String uriPattern, ComponentDef componentDef) {
        if(!uriMapping.containsKey(uriPattern)) {
            Router router = this.routerRegistry.getRouter();
            SeasarResourceFinder finder = new SeasarResourceFinder(router.getContext(), componentDef);
            uriMapping.put(uriPattern, finder);
        }
        return uriMapping.get(uriPattern);
    }
}
