package org.example;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class LoggerInvocationHandler implements MethodInterceptor {

    private Object target;

    public LoggerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println(method.getName() + "方法开始执行;cglib");
        return method.invoke(target, objects);

    }
}
