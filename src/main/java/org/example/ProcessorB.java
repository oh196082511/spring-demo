package org.example;

@Component
public class ProcessorB implements SmartInstantiationAwareBeanPostProcessor {

    @Override
    public void postProcessProperties(DemoApplicationContext applicationContext, Object bean, String beanName) {
        System.out.println("调用ProcessorB.postProcessProperties: " + beanName);
    }
}
