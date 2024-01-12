package org.example;

@Component
public class ProcessorA implements SmartInstantiationAwareBeanPostProcessor {

    @Autowired
    private ProcessorB processorB;

    @Override
    public void postProcessProperties(DemoApplicationContext applicationContext, Object bean, String beanName) {
        System.out.println("调用ProcessorA.postProcessProperties: " + beanName);
    }
}
