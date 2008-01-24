package org.seasar.rest.representation;

import java.util.List;

import org.restlet.resource.Representation;

public interface SeasarRepresentaionVariant {
    Object unmarshall(Representation incoming, Class<?> toClass);
    Representation listRepresentation(List<Object> resourceList);
    Representation entryRepresentation(Object resource);
}