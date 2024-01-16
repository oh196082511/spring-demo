package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 所有被该注解标记的类，都会被代理，代理类会打印日志
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Logger {
}
