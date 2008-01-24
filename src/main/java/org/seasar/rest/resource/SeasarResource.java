package org.seasar.rest.resource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;
import org.seasar.framework.log.Logger;
import org.seasar.rest.context.RestletContext;
import org.seasar.rest.method.DeleteHandler;
import org.seasar.rest.method.GetHandler;
import org.seasar.rest.method.PostHandler;
import org.seasar.rest.method.PutHandler;
import org.seasar.rest.representation.SeasarRepresentation;


public class SeasarResource extends Resource {
    private static final Logger log = Logger.getLogger(SeasarResource.class);

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
        
        for (MediaType mediaType : mediaTypes) {
            getVariants().add(new Variant(mediaType));
        }
        log.debug("SeasarResource creation done.");
    }
    
    private List<MediaType> mediaTypes = new ArrayList<MediaType>();
    public void addVariant(String mediaTypeName) {
        mediaTypes.add(MediaType.valueOf(mediaTypeName));
    }
    
    private Object theResource;

    private GetHandler getHandler;
    private PostHandler postHandler;
    private PutHandler putHandler;
    private DeleteHandler deleteHandler;

    private SeasarRepresentation seasarRepresentation;
    private RestletContext restletContext;

    public void setSeasarRepresentation(SeasarRepresentation seasarRepresentation) {
        this.seasarRepresentation = seasarRepresentation;
    }
    
    public void setRestletContext(RestletContext restletContext) {
        this.restletContext = restletContext;
    }


    @Override
    public Representation getRepresentation(Variant variant) {
        return this.seasarRepresentation.outGoing(variant, this.theResource);
    }
    
    
    @Override
    public boolean allowGet() {
        return this.getHandler.isActivated();
    }
    public void locate() {
        if(allowGet()) {
            this.getHandler.handle();
            this.theResource = this.restletContext.getResource();
        } else {
            throw new UnsupportedOperationException("PUT/DELETE works only with GET on the same URI");
        }
    }
    public void activateGet(Method javaMethod) {
        this.getHandler.activateWith(javaMethod);
    }
    public void setGetHandler(GetHandler getHandler) {
        this.getHandler = getHandler;
    }
    
    
    @Override
    public boolean allowPost() {
        return this.postHandler.isActivated();
    }
    @Override
    public void post(Representation entity) {
        this.postHandler.handle(entity);
    }
    public void activatePost(Method method) {
        this.postHandler.activateWith(method);
    }
    public void setPostHandler(PostHandler postHandler) {
        this.postHandler = postHandler;
    }
    
    
    @Override
    public boolean allowPut() {
        return this.putHandler.isActivated();
    }
    @Override
    public void put(Representation entity) {
        this.putHandler.handle(entity);
    }
    public void activatePut(Method method) {
        this.putHandler.activateWith(method);
    }
    public void setPutHandler(PutHandler putHandler) {
        this.putHandler = putHandler;
    }
    
    
    @Override
    public boolean allowDelete() {
        return this.deleteHandler.isActivated();
    }
    @Override
    public void delete() {
        this.deleteHandler.handle();
    }
    public void activateDelete(Method method) {
        this.deleteHandler.activateWith(method);
    }
    public void setDeleteHandler(DeleteHandler deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
}
