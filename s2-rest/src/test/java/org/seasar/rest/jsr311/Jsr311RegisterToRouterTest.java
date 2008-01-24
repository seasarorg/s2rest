package org.seasar.rest.jsr311;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.Route;
import org.restlet.Router;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.rest.testing.RouterRegistryForTesting;


public class Jsr311RegisterToRouterTest {
    Map<String, Restlet> finders;
    S2Container container;
    
    @Before
    public void setUp() {
        finders = new HashMap<String, Restlet>();
        
        RouterRegistryForTesting.setRouterForTesting(new Router() {
            @Override
            public Route attach(String uriPattern, Restlet target) {
                finders.put(uriPattern, target);
                return super.attach(uriPattern, target);
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
    }
    
    
    @Test
    public void routes() throws Exception {
        assertFalse(finders.isEmpty());
        assertThat(finders.size(), is(6));
        assertThat(finders.containsKey("/employee/list"), is(true));
        assertThat(finders.containsKey("/employee/{empId}"), is(true));
        assertThat(finders.containsKey("/employeeService"), is(true));
        assertThat(finders.containsKey("/employeeService/{employeeId}"), is(true));
    }
    
    @Test
    public void pathAnnotationIsAssignedOnlyForMethods() throws Exception {
        finders.remove("/employee/list");
        finders.remove("/employee/{empId}");
        finders.remove("/employeeService");
        finders.remove("/employeeService/{employeeId}");
        assertThat(finders.size(), is(2));
        
        assertThat(finders.containsKey("/employeePath"), is(true));
        assertThat(finders.containsKey("/employeePath/{employeeId}"), is(true));
    }
    
    @Test
    public void startsWithSlash() throws Exception {
        finders.remove("/employee/list");
        finders.remove("/employee/{empId}");
        finders.remove("/employeeService");
        finders.remove("/employeeService/{employeeId}");
        assertThat(finders.size(), is(2));
        
        for (Iterator<String> iterator = finders.keySet().iterator(); iterator.hasNext();) {
            String uri = iterator.next();
            assertTrue(uri.startsWith("/"));
        }
    }
    
}
