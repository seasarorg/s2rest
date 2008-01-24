package org.seasar.rest.jsr311;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Test;
import org.restlet.util.Variable;
import org.seasar.framework.util.ClassUtil;
import org.seasar.rest.register.impl.Jsr311ParamAnnotationUtil;

import com.example.entity.Employee;


public class Jsr311ParamAnnotationUtilTest {
    public static interface ParamHintSample {
        @GET
        @Path("empSample/{empId}")
        Employee getLongId(@PathParam("empId") long id);
    }
    
    @Test
    public void paramHint() throws Exception {
        Map<String, Variable> variables = new HashMap<String, Variable>();
        
        Method method = ClassUtil.getMethod(ParamHintSample.class, "getLongId", new Class[]{long.class});
        
        Jsr311ParamAnnotationUtil.collectVariableHintFor(method, variables);
        
        Variable actual = variables.get("empId");
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getType(), is(Variable.TYPE_DIGIT));
    }
}
