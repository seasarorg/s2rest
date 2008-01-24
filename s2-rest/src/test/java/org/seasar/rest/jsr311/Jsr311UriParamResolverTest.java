package org.seasar.rest.jsr311;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Before;
import org.junit.Test;
import org.seasar.framework.util.ClassUtil;
import org.seasar.rest.uri.impl.Jsr311UriParamResolver;


public class Jsr311UriParamResolverTest {
    
    public static class Jsr311ArgumentSampleClass {
        
        @GET
        @Path("hoge/{key}")
        public String findHoge(@PathParam("key") String str) {
            return "hoge";
        }

        @GET
        @Path("hoge/{foo}/{bar}")
        public String multiHoge(@PathParam("bar") String str1, @PathParam("foo") String str2) {
            return "hoge";
        }
    }
    
    @Before
    public void setUp() {
        resolver = new Jsr311UriParamResolver();
    }
    Jsr311UriParamResolver resolver;
    
    
    @Test
    public void uriParam() throws Exception {
        Method method = ClassUtil.getMethod(Jsr311ArgumentSampleClass.class, "findHoge", new Class[]{String.class});
        String[] args = resolver.argumentNamesFor(method);
        assertThat(args.length, is(1));
        assertThat(args[0], is("key"));
    }
    
    @Test
    public void multiParam() throws Exception {
        Method method = ClassUtil.getMethod(Jsr311ArgumentSampleClass.class, "multiHoge", new Class[]{String.class, String.class});
        String[] args = resolver.argumentNamesFor(method);
        assertThat(args.length, is(2));
        assertThat(args[0], is("bar"));
        assertThat(args[1], is("foo"));
    }
}
