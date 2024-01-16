package org.example;

/**
 * 对标spring的SmartInstantiationAwareBeanPostProcessor
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

    /**
     * 循环依赖时，提前暴露bean
     */
    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
