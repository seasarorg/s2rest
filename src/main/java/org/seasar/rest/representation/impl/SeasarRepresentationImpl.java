package org.seasar.rest.representation.impl;

import java.util.Arrays;
import java.util.List;

import org.restlet.resource.Representation;
import org.restlet.resource.Variant;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;
import org.seasar.rest.representation.SeasarRepresentaionVariant;
import org.seasar.rest.representation.SeasarRepresentation;


public class SeasarRepresentationImpl implements SeasarRepresentation {
    private static final Logger log = Logger.getLogger(SeasarRepresentationImpl.class);

    public Object inComing(Representation inComingRepresentation, Class<?> toClass) {
//        if(this.inComingRepresentation.getMediaType().equals(MediaType.APPLICATION_JSON, true)) {
//            obj = unmarshall(this.inComingRepresentation, parameterTypes[0]);
//        }
        
        // TODO consider case-insensitive case
        SeasarRepresentaionVariant rep = SingletonS2Container.getComponent(inComingRepresentation.getMediaType().getName());
        return rep.unmarshall(inComingRepresentation, toClass);
    }

    @SuppressWarnings("unchecked")
    public Representation outGoing(Variant variant, Object theResource) {
        log.info("getRepresentation CALLED");
        
        SeasarRepresentaionVariant rep = SingletonS2Container.getComponent(variant.getMediaType().getName());
        
        Representation representation = null;
        if(theResource instanceof List) {
            representation = listRepresentaion(rep, variant, (List) theResource);
        } else if(theResource instanceof Object[]) {
            representation = listRepresentaion(rep, variant, Arrays.asList((Object[]) theResource));
        } else {
            representation = entryRepresentaion(rep, variant, theResource);
        }
        
        return representation;
    }

    private Representation entryRepresentaion(SeasarRepresentaionVariant rep, Variant variant, Object resource) {
        return rep.entryRepresentation(resource);
    }

    private Representation listRepresentaion(SeasarRepresentaionVariant rep, Variant variant, List<Object> resourceList) {
        return rep.listRepresentation(resourceList);
    }
}
