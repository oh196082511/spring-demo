package org.example;

import net.sf.cglib.proxy.Enhancer;

public class LoggerProxyCreator implements SmartInstantiationAwareBeanPostProcessor {

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        Logger annotation = bean.getClass().getAnnotation(Logger.class);
        if (annotation == null) {
            return bean;
        }
        System.out.println("LoggerProxyCreator.getEarlyBeanReference;准备将bean进行代理;beanName: " + beanName);
        // 只有被Logger注解标记的类才会被代理
        return wrapIfNecessary(bean);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Logger annotation = bean.getClass().getAnnotation(Logger.class);
        if (annotation == null) {
            return bean;
        }
        System.out.println("LoggerProxyCreator.postProcessAfterInitialization;准备将bean进行代理;beanName: " + beanName);
        // 只有被Logger注解标记的类才会被代理
        return wrapIfNecessary(bean);
    }

    /**
     * Wrap the given bean if necessary, i.e. if it is eligible for being proxied.
     * @param bean the raw bean instance
     * @return a proxy wrapping the bean, or the raw bean instance as-is
     */
    protected Object wrapIfNecessary(Object bean) {
        return createProxy(bean);
    }

    protected Object createProxy(Object bean) {
        LoggerInvocationHandler loggerInvocationHandler = new LoggerInvocationHandler(bean);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(loggerInvocationHandler);
        enhancer.setSuperclass(bean.getClass());
        return enhancer.create();
    }
}
