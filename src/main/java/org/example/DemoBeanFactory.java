package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对标spring的BeanFactory的实现类
 * 只实现部分功能
 */
public class DemoBeanFactory implements BeanFactory {

    /**
     * 能否允许相同的beanName覆盖BeanDefinition
     */
    private boolean allowBeanDefinitionOverriding = true;

    /** Map of bean definition objects, keyed by bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    /**
     * Check if this bean factory contains a bean definition with the given name.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     * @param beanName the name of the bean to look for
     * @return if this bean factory contains a bean definition with the given name
     */
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        BeanDefinition oldBeanDefinition = beanDefinitionMap.get(beanName);
        if (oldBeanDefinition != null) {
            if (!allowBeanDefinitionOverriding) {
                throw new RuntimeException("Cannot register bean definition [" + beanDefinition + "] for bean '" +
                        beanName + "': There is already [" + oldBeanDefinition + "] bound.");
            }
        }
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
