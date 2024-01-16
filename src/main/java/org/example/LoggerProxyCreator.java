package org.example;

import net.sf.cglib.proxy.Enhancer;

import java.util.HashMap;
import java.util.Map;

public class LoggerProxyCreator implements SmartInstantiationAwareBeanPostProcessor {

    private Map<Object, Object> beanName2Proxy = new HashMap<>();

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
        if (beanName2Proxy.containsKey(bean)) {
            return beanName2Proxy.get(bean);
        }
        Object proxy = createProxy(bean);
        beanName2Proxy.put(bean, proxy);
        return proxy;
    }

    protected Object createProxy(Object bean) {
        LoggerInvocationHandler loggerInvocationHandler = new LoggerInvocationHandler(bean);
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(loggerInvocationHandler);
        enhancer.setSuperclass(bean.getClass());
        return enhancer.create();
    }
}
