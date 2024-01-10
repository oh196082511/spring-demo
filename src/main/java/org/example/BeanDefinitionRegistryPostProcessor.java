package org.example;

/**
 * 对标spring的BeanDefinitionRegistryPostProcessor
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     * @param registry the bean definition registry used by the application context
     */
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

}
