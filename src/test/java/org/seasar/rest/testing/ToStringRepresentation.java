package org.seasar.rest.testing;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.seasar.rest.representation.SeasarRepresentaionVariant;

public class ToStringRepresentation implements SeasarRepresentaionVariant {

    public Representation entryRepresentation(Object resource) {
        return new StringRepresentation(ReflectionToStringBuilder.toString(resource, ToStringStyle.SHORT_PREFIX_STYLE));
    }

    public Representation listRepresentation(List<Object> resourceList) {
        return new StringRepresentation(ReflectionToStringBuilder.toString(resourceList, ToStringStyle.SHORT_PREFIX_STYLE));
    }

    public Object unmarshall(Representation incoming, Class<?> toClass) {
        throw new UnsupportedOperationException("unmarshall is not supported");
    }

}
