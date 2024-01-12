package org.example;

/**
 * 对标spring的InstantiationAwareBeanPostProcessor
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 为bean注入属性前,扩展点
     * @param bean
     * @param beanName
     * @return
     */
    default void postProcessProperties(DemoApplicationContext applicationContext, Object bean, String beanName) {

    }
}
