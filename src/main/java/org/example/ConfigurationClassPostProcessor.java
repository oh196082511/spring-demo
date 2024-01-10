package org.example;

/**
 * 对标spring的ConfigurationClassPostProcessor
 * 负责将注解的类放入BeanDefinitionMap中
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // 负责将注解的类放入BeanDefinitionMap中
        // TODO

    }
}
