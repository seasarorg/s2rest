package org.seasar.rest.jsr311;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.Route;
import org.restlet.Router;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.rest.resource.SeasarResourceFinder;
import org.seasar.rest.resource.SeasarResource;
import org.seasar.rest.testing.RouterRegistryForTesting;


public class Jsr311CustomizerTest {
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
        S2ContainerFactory.destroy();
    }
    
    
    @Test
    public void allowMethods() throws Exception {
        Restlet finder = finders.get("/employee/list");
//        assertNotNull(finder);
        assertThat(finder, is(not(nullValue())));
        assertThat(finder, is(instanceOf(SeasarResourceFinder.class)));
        
        SeasarResourceFinder srf = (SeasarResourceFinder) finder;
        Request request = new Request(Method.POST, "/employee/list");
        Resource resource = srf.findTarget(request, null);
        assertThat(resource, is(not(nullValue())));
        assertThat(resource, is(instanceOf(SeasarResource.class)));
        
        SeasarResource sr = (SeasarResource) resource;
        assertThat(sr.allowPost(), is(true));
        assertThat(sr.allowGet(), is(true));
        assertThat(sr.allowPut(), is(false));
        assertThat(sr.allowDelete(), is(false));
    }
    

    @Test
    public void httpGetAccess() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) finders.get("/employeeService");
        Request request = new Request(Method.GET, "/employeeService");
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        assertThat(sr.allowGet(), is(true));
        assertThat(sr.allowPost(), is(false));
        assertThat(sr.allowPut(), is(false));
        assertThat(sr.allowDelete(), is(false));
    }


    @Test
    public void httpGetAccessWithArgs() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) finders.get("/employeeService/{employeeId}");
        Request request = new Request(Method.GET, "/employeeService/5");
        Map<String, Object> attr = request.getAttributes();
        attr.put("employeeId", 5);
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        MockInterceptor mock = SingletonS2Container.getComponent("mock");
        assertTrue(mock.isInvoked("findById"));
        
        assertThat(sr.allowGet(), is(true));
        assertThat(sr.allowPost(), is(false));
        assertThat(sr.allowPut(), is(false));
        assertThat(sr.allowDelete(), is(false));
    }
    
    
    @Test
    public void variants() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) finders.get("/employee/{empId}");
        Request request = new Request(Method.GET, "/employee/5");
        Map<String, Object> attr = request.getAttributes();
        attr.put("empId", 5);
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        List<Variant> variants = sr.getVariants();
        assertThat(variants.size(), is(5));
    }


    @Test
    public void methodLevelVariants() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) finders.get("/employeeService/{employeeId}");
        Request request = new Request(Method.GET, "/employeeService/5");
        Map<String, Object> attr = request.getAttributes();
        attr.put("employeeId", 8);
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        List<Variant> variants = sr.getVariants();
        assertThat(variants.size(), is(5));
    }


    @Test
    public void methodLevelVariants2() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) finders.get("/employeeService");
        Request request = new Request(Method.GET, "/employeeService");
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        List<Variant> variants = sr.getVariants();
        assertThat(variants.size(), is(3));
    }
}
