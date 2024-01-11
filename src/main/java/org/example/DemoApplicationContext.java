package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对标spring的abstractApplicationContext+BeanFactory+实现类
 * 只实现部分功能
 */
public class DemoApplicationContext implements BeanDefinitionRegistry {

    public DemoApplicationContext() {
        // 注册相关的postProcessors
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this);
    }

    /**
     * 能否允许相同的beanName覆盖BeanDefinition
     */
    private boolean allowBeanDefinitionOverriding = true;

    /** Map of bean definition objects, keyed by bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

    /** BeanFactoryPostProcessors to apply on refresh. */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    /** List of bean definition names, in registration order. */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    public void refresh() {


        // Invoke factory processors registered as beans in the context.
        invokeBeanFactoryPostProcessors();


    }

    /**
     * Return the list of BeanFactoryPostProcessors that will get applied
     * to the internal BeanFactory.
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    /**
     * (spring里代码写在代理类，这里直接实现)
     * Instantiate and invoke all registered BeanFactoryPostProcessor beans,
     * respecting explicit order if given.
     * <p>Must be called before singleton instantiation.
     */
    protected void invokeBeanFactoryPostProcessors() {

        // BeanDefinitionRegistryPostProcessor相关的processors
        List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
        String[] postProcessorNames = getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
        for (String ppName : postProcessorNames) {
            BeanDefinitionRegistryPostProcessor bean = getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
            currentRegistryProcessors.add(bean);
        }
        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, this);
    }

    /**
     * Invoke the given BeanDefinitionRegistryPostProcessor beans.
     */
    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }

    /**
     * Check if this bean factory contains a bean definition with the given name.
     * <p>Does not consider any hierarchy this factory may participate in,
     * and ignores any singleton beans that have been registered by
     * other means than bean definitions.
     * @param beanName the name of the bean to look for
     * @return if this bean factory contains a bean definition with the given name
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        BeanDefinition oldBeanDefinition = beanDefinitionMap.get(beanName);
        if (oldBeanDefinition != null) {
            if (!allowBeanDefinitionOverriding) {
                throw new RuntimeException("Cannot register bean definition [" + beanDefinition + "] for bean '" +
                        beanName + "': There is already [" + oldBeanDefinition + "] bound.");
            }
        }
        beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[0]);
    }


    /**
     * 对标ListableBeanFactory里的getBeanNamesForType接口的实现
     * 简单实现
     */
    public String[] getBeanNamesForType(Class<?> type) {
        return beanDefinitionMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof DemoBeanDefinition)
                .filter(entry -> type.isAssignableFrom(((DemoBeanDefinition) entry.getValue()).getBeanClass()))
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    /**
     * 对标BeanFactory里的getBean接口的实现
     * get不到则实时创建，三级缓存逻辑也在这里
     * 简单实现
     */
    public <T> T getBean(String name, Class<T> requiredType) {
        // TODO 后续三级缓存实现
        try {
            DemoBeanDefinition demoBeanDefinition = (DemoBeanDefinition) beanDefinitionMap.get(name);
            return (T) demoBeanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getBean error");
        }
    }

}
