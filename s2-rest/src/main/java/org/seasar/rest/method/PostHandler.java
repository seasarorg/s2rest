package org.seasar.rest.method;

import org.restlet.resource.Representation;

public interface PostHandler extends Activatable {
    void handle(Representation inComingRepresentation);
}