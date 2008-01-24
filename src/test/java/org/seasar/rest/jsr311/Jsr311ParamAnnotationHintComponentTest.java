package org.seasar.rest.jsr311;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.Route;
import org.restlet.Router;
import org.restlet.util.Variable;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.rest.testing.RouterRegistryForTesting;


public class Jsr311ParamAnnotationHintComponentTest {
    Map<String, Route> routes;
    S2Container container;
    
    @Before
    public void setUp() {
        routes = new HashMap<String, Route>();
        
        RouterRegistryForTesting.setRouterForTesting(new Router() {
            @Override
            public Route attach(String uriPattern, Restlet target) {
                Route route = super.attach(uriPattern, target);
                routes.put(uriPattern, route);
                return route;
            }
        });

        S2ContainerFactory.configure("org/seasar/rest/jsr311/s2container.dicon");
        container = S2ContainerFactory.create("org/seasar/rest/jsr311/test.dicon");
        container.init();
        SingletonS2ContainerFactory.setContainer(container);
    }
    
    @After
    public void tearDown() {
        RouterRegistryForTesting.setRouterForTesting(null);
        SingletonS2ContainerFactory.setContainer(null);
        if(container != null) {
            container.destroy();
        }
        SingletonS2ContainerFactory.destroy();
        S2ContainerFactory.destroy();
    }
    
    
    @Test
    public void variableHint() throws Exception {
        Route route = routes.get("/employee/{empId}");
        Map<String, Variable> variables = route.getTemplate().getVariables();
        Variable actual = variables.get("empId");
        
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getType(), is(Variable.TYPE_DIGIT));
    }
    
}
