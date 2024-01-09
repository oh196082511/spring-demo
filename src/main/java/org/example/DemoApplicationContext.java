package org.example;

/**
 * 对标spring的某个具体实现context类
 * 实现部分功能
 */
public class DemoApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DemoBeanFactory beanFactory;

    public DemoApplicationContext() {
        // 初始化一个beanFactory
        beanFactory = new DemoBeanFactory();
        // 注册相关的postProcessors
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    public DemoBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
    }
}
