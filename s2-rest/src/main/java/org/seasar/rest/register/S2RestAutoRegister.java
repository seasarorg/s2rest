package org.seasar.rest.register;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.autoregister.AbstractComponentTargetAutoRegister;
import org.seasar.framework.log.Logger;

public class S2RestAutoRegister extends AbstractComponentTargetAutoRegister {
    private static final Logger log = Logger.getLogger(S2RestAutoRegister.class);

    private Jsr311ComponentRegister componentRegister;
    public void setComponentRegister(Jsr311ComponentRegister componentRegister) {
        this.componentRegister = componentRegister;
    }

    @Override
    protected void register(ComponentDef componentDef) {
        log.debug("S2RestAutoRegister.register start");
        this.componentRegister.register(componentDef);
        log.debug("S2RestAutoRegister.register end");
    }

}
