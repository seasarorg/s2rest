package org.seasar.rest.testing;

import org.restlet.Router;
import org.seasar.rest.register.RouterRegistry;

public class RouterRegistryForTesting implements RouterRegistry {
    private static Router router;
    public static void setRouterForTesting(Router r) {
        router = r;
    }
    public static Router getRouterForTesting() {
        return router;
    }
    
    public Router getRouter() {
        return router;
    }
}
