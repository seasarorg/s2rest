package org.seasar.rest.register;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.customizer.AbstractCustomizer;
import org.seasar.framework.log.Logger;

public class Jsr311Customizer extends AbstractCustomizer {
    private static final Logger log = Logger.getLogger(Jsr311Customizer.class);

    private Jsr311ComponentRegister componentRegister;
    public void setComponentRegister(Jsr311ComponentRegister componentRegister) {
        this.componentRegister = componentRegister;
    }

    @Override
    protected void doCustomize(ComponentDef componentDef) {
        log.debug("Jsr311Customizer.doCustomize start");
        this.componentRegister.register(componentDef);
        log.debug("Jsr311Customizer.doCustomize end");
    }
}
