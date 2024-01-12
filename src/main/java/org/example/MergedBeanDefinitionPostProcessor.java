package org.example;

/**
 * 对标spring的AutowiredAnnotationBeanPostProcessor
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {

    /**
     * Post-process the given merged bean definition for the specified bean.
     * @param beanType the actual type of the managed bean instance
     * @param beanName the name of the bean
     */
    void postProcessMergedBeanDefinition(Class<?> beanType, String beanName);

}
