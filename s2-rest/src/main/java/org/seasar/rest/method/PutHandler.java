package org.seasar.rest.method;

import org.restlet.resource.Representation;

public interface PutHandler extends Activatable {
    void handle(Representation inComingRepresentation);
}