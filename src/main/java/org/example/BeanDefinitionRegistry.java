package org.example;

/**
 * 对标spring的BeanDefinitionRegistry
 * 管理BeanDefinition
 */
public interface BeanDefinitionRegistry {

    /**
     * Check if this registry contains a bean definition with the given name.
     * @param beanName the name of the bean to look for
     * @return if this registry contains a bean definition with the given name
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Register a new bean definition with this registry.
     * Must support RootBeanDefinition and ChildBeanDefinition.
     * @param beanName the name of the bean instance to register
     * @param beanDefinition definition of the bean instance to register
     * or if there is already a BeanDefinition for the specified bean name
     * (and we are not allowed to override it)
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * Return the names of all beans defined in this registry.
     * @return the names of all beans defined in this registry,
     * or an empty array if none defined
     */
    String[] getBeanDefinitionNames();

}
