package org.seasar.rest.testing;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.PathResolver;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultConfigurator;
import org.seasar.framework.container.factory.S2ContainerFactory.Provider;

/**
 * custom Configurator for ease of testing
 * @author t-wada
 */
public class ConfiguratorForTesting extends DefaultConfigurator {
    private String coolDeployDiconPath = "cooldeploy-autoregister.dicon";
    
    public void setCoolDeployDiconPath(String path) {
        this.coolDeployDiconPath = path;
    }

    protected Provider createProvider(S2Container configurationContainer) {
        ProviderForTesting provider = new ProviderForTesting(this.coolDeployDiconPath);
        if (configurationContainer.hasComponentDef(PathResolver.class)) {
            provider.setPathResolver((PathResolver) configurationContainer
                    .getComponent(PathResolver.class));
        }
        if (configurationContainer.hasComponentDef(ExternalContext.class)) {
            provider
                    .setExternalContext((ExternalContext) configurationContainer
                            .getComponent(ExternalContext.class));
        }
        if (configurationContainer
                .hasComponentDef(ExternalContextComponentDefRegister.class)) {
            provider
                    .setExternalContextComponentDefRegister((ExternalContextComponentDefRegister) configurationContainer
                            .getComponent(ExternalContextComponentDefRegister.class));
        }
        return provider;
    }

}
