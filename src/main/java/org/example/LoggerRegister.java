package org.example;

public class LoggerRegister implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("LoggerRegister.postProcessBeanDefinitionRegistry;准备将LoggerProxyCreator的definition注册到context中");
        // 注册bean
        registry.registerBeanDefinition("org.example.LoggerProxyCreator", new DemoBeanDefinition(LoggerProxyCreator.class));
    }
}
