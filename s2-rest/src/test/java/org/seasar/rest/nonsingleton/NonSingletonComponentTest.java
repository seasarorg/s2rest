package org.seasar.rest.nonsingleton;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.Route;
import org.restlet.Router;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.rest.resource.SeasarResourceFinder;
import org.seasar.rest.resource.SeasarResource;
import org.seasar.rest.testing.RouterRegistryForTesting;


public class NonSingletonComponentTest {
    Map<String, Restlet> routes;
    S2Container container;
    
    @Before
    public void setUp() {
        routes = new HashMap<String, Restlet>();
        
        RouterRegistryForTesting.setRouterForTesting(new Router() {
            @Override
            public Route attach(String uriPattern, Restlet target) {
                routes.put(uriPattern, target);
                return null;
            }
        });

        S2ContainerFactory.configure("org/seasar/rest/nonsingleton/s2container.dicon");
        container = S2ContainerFactory.create("org/seasar/rest/nonsingleton/test.dicon");
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
    
    
    @Ignore("pending in case of huge 0.6 changes")
    @Test
    public void routes() throws Exception {
        assertFalse(routes.isEmpty());
        assertThat(routes.size(), is(5));
        assertThat(routes.containsKey("/empLogic/{employeeId}"), is(true));
        assertThat(routes.containsKey("/employees"), is(true));
        assertThat(routes.containsKey("/employee/{employeeId}"), is(true));
        assertThat(routes.containsKey("/employeeService"), is(true));
        assertThat(routes.containsKey("/employeeService/{employeeId}"), is(true));
    }
    

    @Ignore("pending in case of huge 0.6 changes")
    @Test
    public void uriToPropertyMapping() throws Exception {
        SeasarResourceFinder srf = (SeasarResourceFinder) routes.get("/empLogic/{employeeId}");
        Request request = new Request(Method.GET, "/empLogic/7");
        Map<String, Object> attr = request.getAttributes();
        attr.put("employeeId", 7);
        SeasarResource sr = (SeasarResource) srf.findTarget(request, null);
        
        MockInterceptor mock = SingletonS2Container.getComponent("mock");
        assertTrue(mock.isInvoked("getById"));
        
        assertThat(mock.getArgs("getById"), is(not(nullValue())));
        assertThat(mock.getArgs("getById")[0], is((Object)new Long(7)));
    }
    
}
