package org.seasar.rest.testing;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory.DefaultProvider;

/**
 * custom Provider for ease of testing
 * @author t-wada
 */
public class ProviderForTesting extends DefaultProvider {
    private final String diconPath;

    public ProviderForTesting(String diconPath) {
        this.diconPath = diconPath;
    }
    
    public S2Container create(final String path) {
        final S2Container container = super.create(path);
        include(container, diconPath);
        return container;
    }

}
