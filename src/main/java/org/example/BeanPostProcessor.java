package org.example;

/**
 * 对标spring的BeanPostProcessor
 */
public interface BeanPostProcessor {

    /**
     * 初始化前，如果实现了，会提前返回bean，不再进行后续初始化
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后，如果实现了，会返回最终bean
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
