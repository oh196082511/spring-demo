package org.example;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;

public class InjectionMetadata {

    private final Class<?> targetClass;

    private final Collection<InjectedElement> injectedElements;

    public Collection<InjectedElement> getInjectedElements() {
        return injectedElements;
    }

    /**
     * Create a new {@code InjectionMetadata instance}.
     * instance in case of no elements.
     * @param targetClass the target class
     * @param elements the associated elements to inject
     */
    public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
        this.targetClass = targetClass;
        this.injectedElements = elements;
    }


    /**
     * A single injected element.
     */
    public static class InjectedElement {

        protected final Member member;

        protected InjectedElement(Member member) {
            this.member = member;
        }
    }


}
