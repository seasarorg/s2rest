package org.seasar.rest.representation;

import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

public interface SeasarRepresentation {
    Object inComing(Representation inComingRepresentation, Class<?> toClass);
    Representation outGoing(Variant variant, Object theResource);
}