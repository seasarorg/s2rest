package org.seasar.rest.functional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Context;
import org.restlet.Router;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.rest.RestfulSeasarApplication;
import org.seasar.rest.testing.RouterRegistryForTesting;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;


public class Jsr311FunctionalTest {
    private static final Logger log = Logger.getLogger(Jsr311FunctionalTest.class);
    WebConversation wc;
    Server server;
    S2Container container;
    
    @Before
    public void setUp() throws Exception {
        wc = new WebConversation();
        wc.setExceptionsThrownOnErrorStatus(false);
        
        RestfulSeasarApplication app = new RestfulSeasarApplication(new Context());
        RouterRegistryForTesting.setRouterForTesting((Router) app.getRoot());
        
        S2ContainerFactory.destroy();
        S2ContainerFactory.configure("org/seasar/rest/null_s2container.dicon");
        container = S2ContainerFactory.create("org/seasar/rest/functional/VariantTest.dicon");
        container.init();
        SingletonS2ContainerFactory.setContainer(container);
        
        server = new Server(Protocol.HTTP, 8182, app);
        server.start();
    }
    
    @After
    public void tearDown() throws Exception {
        RouterRegistryForTesting.setRouterForTesting(null);
        SingletonS2ContainerFactory.setContainer(null);
        if(container != null) {
            container.destroy();
        }
        if (server != null && server.isStarted() && !server.isStopped()) {
            server.stop();
        }
        SingletonS2ContainerFactory.destroy();
    }
    
    @Test
    public void chooseRepresentationByAcceptHeader() throws Exception {
        GetMethodWebRequest req =
            new GetMethodWebRequest("http://localhost:8182/employee/100");
        req.setHeaderField("Accept", "text/plain");
        
        WebResponse resp = wc.getResource(req);
        
        assertThat(resp.getResponseCode(), is(200));
        assertThat(resp.getText(), is("Employee[employeeId=100,employeeName=Hoge]"));
    }
    
}
