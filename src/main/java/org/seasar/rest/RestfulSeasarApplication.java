package org.seasar.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;

public class RestfulSeasarApplication extends Application {
    
    public RestfulSeasarApplication(Context context) {
        super(context);
    }
    
    @Override
    public Restlet createRoot() {
        return new Router(getContext());
    }

}
