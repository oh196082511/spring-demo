package org.example;

/**
 * 对标spring的BeanDefinition的实现类
 * 只实现部分功能
 */
public class DemoBeanDefinition implements BeanDefinition {

    private volatile Object beanClass;

    /**
     * Create a new RootBeanDefinition for a singleton.
     * @param beanClass the class of the bean to instantiate
     * @see #setBeanClass
     */
    public DemoBeanDefinition(Class<?> beanClass) {
        super();
        setBeanClass(beanClass);
    }

    /**
     * Specify the class for this bean.
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}
