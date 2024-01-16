package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对标spring的abstractApplicationContext+BeanFactory+实现类
 * 只实现部分功能
 */
public class DemoApplicationContext implements BeanDefinitionRegistry {

    public DemoApplicationContext() {
        System.out.println("DemoApplicationContext.DemoApplicationContext开始初始化");
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

    /** Cache of pre-filtered post-processors. */
    private volatile BeanPostProcessorCache beanPostProcessorCache;

    /** BeanPostProcessors to apply. */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * Cache of singleton objects: bean name to bean instance.
     * 一级缓存，存储最终完成的bean实例
     * */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * Cache of early singleton objects: bean name to bean instance.
     * 二级缓存，存储实例化但未完成初始化的bean实例
     * */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /**
     * Cache of singleton factories: bean name to ObjectFactory.
     * 三级缓存，存储未来创造bean的lambda表达式
     * */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    public void refresh() {
        System.out.println("DemoApplicationContext.refresh开始refresh");


        // Invoke factory processors registered as beans in the context.
        invokeBeanFactoryPostProcessors();
        // Register bean processors that intercept bean creation.
        registerBeanPostProcessors();

        // Instantiate all remaining (non-lazy-init) singletons.
        finishBeanFactoryInitialization();

    }

    /**
     * Finish the initialization of this context's bean factory,
     * initializing all remaining singleton beans.
     */
    protected void finishBeanFactoryInitialization() {
        // Instantiate all remaining (non-lazy-init) singletons.
        List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);

        // Trigger initialization of all non-lazy singleton beans...
        for (String beanName : beanNames) {
            getBean(beanName, getBeanDefinition(beanName).getBeanClass());
        }
    }

    /**
     * Instantiate and register all BeanPostProcessor beans,
     * respecting explicit order if given.
     * <p>Must be called before any instantiation of application beans.
     */
    protected void registerBeanPostProcessors() {
        System.out.println("开始registerBeanPostProcessors");
        // 初始化AutowiredAnnotationBeanPostProcessor
        // 单独放出来是因为AutowiredAnnotationBeanPostProcessor有资格切其它的processor
        String[] autowired = getBeanNamesForType(AutowiredAnnotationBeanPostProcessor.class);
        if (autowired.length != 1) {
            throw new RuntimeException("AutowiredAnnotationBeanPostProcessor must be and only be one");
        }
        AutowiredAnnotationBeanPostProcessor bean = getBean(autowired[0], AutowiredAnnotationBeanPostProcessor.class);
        beanPostProcessors.add(bean);
        beanPostProcessorCache = null;

        // 所有processor统一处理，后续的所有bean都会被这些processor切
        List<BeanPostProcessor> processors = new ArrayList<>();
        String[] postProcessorNames = getBeanNamesForType(BeanPostProcessor.class);
        for (String ppName : postProcessorNames) {
            BeanPostProcessor beanPostProcessor = getBean(ppName, BeanPostProcessor.class);
            if (beanPostProcessors.contains(beanPostProcessor)) {
                continue;
            }
            processors.add(beanPostProcessor);
        }
        beanPostProcessors.addAll(processors);
        beanPostProcessorCache = null;
        System.out.println("结束registerBeanPostProcessors");
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
        System.out.println("开始invokeBeanFactoryPostProcessors");

        // BeanDefinitionRegistryPostProcessor相关的processors
        List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
        String[] postProcessorNames = getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
        for (String ppName : postProcessorNames) {
            BeanDefinitionRegistryPostProcessor bean = getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
            currentRegistryProcessors.add(bean);
        }
        // 注册所有的BeanDefinitionRegistryPostProcessor
        // 注册完后，后续的bean初始化会被这些processor切
        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, this);
        System.out.println("结束invokeBeanFactoryPostProcessors");
    }

    /**
     * Invoke the given BeanDefinitionRegistryPostProcessor beans.
     */
    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
        System.out.println("----------------------invokeBeanDefinitionRegistryPostProcessors中-----------------------");
        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
        System.out.println("----------------------invokeBeanDefinitionRegistryPostProcessors结束-----------------------");
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
     * Return a merged RootBeanDefinition, traversing the parent bean definition
     * if the specified bean corresponds to a child bean definition.
     * @param beanName the name of the bean to retrieve the merged definition for
     * @return a (potentially merged) RootBeanDefinition for the given bean
     */
    protected DemoBeanDefinition getBeanDefinition(String beanName) {
        return (DemoBeanDefinition) beanDefinitionMap.get(beanName);
    }

    /**
     * 对标BeanFactory里的getBean接口的实现
     * get不到则实时创建，三级缓存逻辑也在这里
     * 简单实现
     */
    public <T> T getBean(String name, Class<T> requiredType) {
        return doGetBean(name, requiredType);
    }

    private <T> T doGetBean(
            String beanName, Class<T> requiredType) {
        System.out.println("尝试getBean，如果有二级、一级缓存，直接获取。beanName=" + beanName);
        Object singleton = getSingletonWithFactoriesGet(beanName);
        if (singleton != null) {
            System.out.println("getBean获取成功，beanName=" + beanName);
            return (T) singleton;
        }
        // 之前没有创建过，第一次开始创建
        return (T) getSingleton(beanName, () -> {
            return createBean(beanName);
        });
    }

    /**
     * Return the (raw) singleton object registered under the given name,
     * creating and registering a new one if none registered yet.
     * @param beanName the name of the bean
     * @param singletonFactory the ObjectFactory to lazily create the singleton
     * with, if necessary
     * @return the registered singleton object
     */
    private Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 由factory方法逻辑保证，创建完整对象(属性自动注入、初始化方法调用、动态代理等等)
            singletonObject = singletonFactory.getObject();
            // 创造完成后，放入一级缓存，同时清除其他缓存
            System.out.println("创建完成，放入一级缓存，同时清除其他缓存，beanName=" + beanName);
            addSingleton(beanName, singletonObject);
        }
        return singletonObject;
    }

    /**
     * Add the given singleton object to the singleton cache of this factory.
     * <p>To be called for eager registration of singletons.
     * @param beanName the name of the bean
     * @param singletonObject the singleton object
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
        this.singletonFactories.remove(beanName);
        this.earlySingletonObjects.remove(beanName);
    }

    /**
     * 创建bean
     */
    private Object createBean(String beanName){
        return doCreateBean(beanName);
    }

    /**
     * Actually create the specified bean. Pre-creation processing has already happened
     * at this point, e.g. checking {@code postProcessBeforeInstantiation} callbacks.
     * <p>Differentiates between default bean instantiation, use of a
     * factory method, and autowiring a constructor.
     * @param beanName the name of the bean
     * @return a new instance of the bean
     */
    protected Object doCreateBean(String beanName) {
        System.out.println("get拿不到，开始创建bean，beanName=" + beanName);
        // Instantiate the bean.
        Object bean = createBeanInstance(beanName);
        System.out.println("bean实例化成功，beanName=" + beanName);

        // Allow post-processors to modify the merged bean definition.
        Class<?> beanType = getBeanDefinition(beanName).getBeanClass();
        applyMergedBeanDefinitionPostProcessors(beanType, beanName);

        // Eagerly cache singletons to be able to resolve circular references
        // even when triggered by lifecycle interfaces like BeanFactoryAware.
        addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, bean));
        System.out.println("将bean放入三级缓存成功，beanName=" + beanName);

        // 为bean填充属性
        populateBean(beanName, bean);
        // 调用一系列扩展点接口
        initializeBean(beanName, bean);

        // 至少返回二级缓存数据
        System.out.println("bean初始化成功，准备返回给外界实际的bean，待后续处理。beanName=" + beanName);
        return getSingletonWithFactoriesGet(beanName);
    }

    /**
     * Apply MergedBeanDefinitionPostProcessors to the specified bean definition,
     * invoking their {@code postProcessMergedBeanDefinition} methods.
     * @param beanType the actual type of the managed bean instance
     * @param beanName the name of the bean
     * @see MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition
     */
    protected void applyMergedBeanDefinitionPostProcessors(Class<?> beanType, String beanName) {
        if (beanType.isAssignableFrom(BeanPostProcessor.class)
                || beanType.isAssignableFrom(BeanFactoryPostProcessor.class)) {
            // 创造processor时，无需使用扩展点
            return;
        }
        System.out.println("----------开始执行MergedBeanDefinitionPostProcessor，beanName=" + beanName + "-------------------------------------------");
        for (MergedBeanDefinitionPostProcessor processor : getBeanPostProcessorCache().mergedDefinition) {
            processor.postProcessMergedBeanDefinition(beanType, beanName);
        }
        System.out.println("----------MergedBeanDefinitionPostProcessor结束，beanName=" + beanName + "-------------------------------------------");

    }

    /**
     * Create a new instance for the specified bean, using an appropriate instantiation strategy:
     * factory method, constructor autowiring, or simple instantiation.
     * @param beanName the name of the bean
     * @return a BeanWrapper for the new instance
     * @see #instantiateBean
     */
    protected Object createBeanInstance(String beanName) {
        return instantiateBean(beanName);
    }

    /**
     * Instantiate the given bean using its default constructor.
     * @param beanName the name of the bean
     * @return the new instance
     */
    protected Object instantiateBean(String beanName) {
        try {
            // 简单实现，spring中使用策略模式
            return ((DemoBeanDefinition) beanDefinitionMap.get(beanName)).getBeanClass().getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("实例化bean失败");
        }

    }

    /**
     *  如果之前有缓存，直接执行并拿到缓存
     *  这里至少返回了二级缓存
     */

    private Object getSingletonWithFactoriesGet(String beanName) {
        // Quick check for existing instance without full singleton lock
        // 有完整则拿完整
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 没有完整，拿二级缓存
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                // Consistent creation of early reference within full singleton lock
                // 没有二级缓存，直接用三级缓存算出二级缓存
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    System.out.println("三级缓存拿到了，准备创建bean，并且将结果放入二级缓存，beanName=" + beanName);
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    /**
     * Add the given singleton factory for building the specified singleton
     * if necessary.
     * <p>To be called for eager registration of singletons, e.g. to be able to
     * resolve circular references.
     * @param beanName the name of the bean
     * @param singletonFactory the factory for the singleton object
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
            }
        }
    }

    /**
     * Obtain a reference for early access to the specified bean,
     * typically for the purpose of resolving a circular reference.
     * @param beanName the name of the bean (for error handling purposes)
     * @param bean the raw bean instance
     * @return the object to expose as bean reference
     */
    protected Object getEarlyBeanReference(String beanName, Object bean) {
        Object exposedObject = bean;
        if (hasInstantiationAwareBeanPostProcessors()) {
            for (SmartInstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().smartInstantiationAware) {
                exposedObject = bp.getEarlyBeanReference(exposedObject, beanName);
            }
        }
        return exposedObject;
    }

    /**
     * Return whether this factory holds a InstantiationAwareBeanPostProcessor
     * that will get applied to singleton beans on creation.
     */
    protected boolean hasInstantiationAwareBeanPostProcessors() {
        return !getBeanPostProcessorCache().instantiationAware.isEmpty();
    }

    /**
     * 实例化后，负责填充bean的属性
     */
    private void populateBean(String beanName, Object bw) {
        System.out.println("----------开始执行populateBean，填充属性。beanName=" + beanName + "-------------------------------------------");
        for (InstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().instantiationAware) {
            bp.postProcessProperties(this, bw, beanName);
        }
        System.out.println("----------populateBean成功。beanName=" + beanName + "-------------------------------------------");

    }

    /**
     * Initialize the given bean instance, applying factory callbacks
     * as well as init methods and bean post processors.
     * <p>Called from {@link #createBean} for traditionally defined beans,
     * and from {@link #initializeBean} for existing bean instances.
     * @param beanName the bean name in the factory (for debugging purposes)
     * @param bean the new bean instance we may need to initialize
     * (can also be {@code null}, if given an existing bean instance)
     * @return the initialized bean instance (potentially wrapped)
     */
    protected Object initializeBean(String beanName, Object bean) {
        Object wrappedBean = bean;
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);


        // TODO 触发各种扩展点

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return bean;
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    /**
     * Return the internal cache of pre-filtered post-processors,
     * freshly (re-)building it if necessary.
     * @since 5.3
     */
    BeanPostProcessorCache getBeanPostProcessorCache() {
        BeanPostProcessorCache bpCache = this.beanPostProcessorCache;
        if (bpCache == null) {
            bpCache = new BeanPostProcessorCache();
            for (BeanPostProcessor bp : this.beanPostProcessors) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    bpCache.instantiationAware.add((InstantiationAwareBeanPostProcessor) bp);
                    if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                        bpCache.smartInstantiationAware.add((SmartInstantiationAwareBeanPostProcessor) bp);
                    }
                }
                if (bp instanceof MergedBeanDefinitionPostProcessor) {
                    bpCache.mergedDefinition.add((MergedBeanDefinitionPostProcessor) bp);
                }
            }
            this.beanPostProcessorCache = bpCache;
        }
        return bpCache;
    }

    /**
     * Internal cache of pre-filtered post-processors.
     * 前面初始化的processor都会放到这里
     * 放入后，才会统一对后面的bean进行处理
     *
     * @since 5.3
     */
    static class BeanPostProcessorCache {

        final List<SmartInstantiationAwareBeanPostProcessor> smartInstantiationAware = new ArrayList<>();

        final List<InstantiationAwareBeanPostProcessor> instantiationAware = new ArrayList<>();

        final List<MergedBeanDefinitionPostProcessor> mergedDefinition = new ArrayList<>();
    }

}
