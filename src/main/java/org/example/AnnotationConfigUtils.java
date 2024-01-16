package org.example;

/**
 * 对标spring的AnnotationConfigUtils
 */
public class AnnotationConfigUtils {

    /**
     * The bean name of the internally managed Configuration annotation processor.
     */
    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.example.ConfigurationClassPostProcessor";

    /**
     * The bean name of the internally managed Autowired annotation processor.
     */
    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.example.AutowiredAnnotationBeanPostProcessor";

    /**
     * The bean name of the internally managed Autowired annotation processor.
     */
    public static final String LOGGER_REGISTER = "org.example.LoggerRegister";

    /**
     * 把相关的postProcessors注册到registry中
     * @param registry the registry to operate on
     */
    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            // 负责把component注解的bean注册到beanFactory中
            DemoBeanDefinition demoBeanDefinition = new DemoBeanDefinition(ConfigurationClassPostProcessor.class);
            registry.registerBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, demoBeanDefinition);
            System.out.println("把负责component的注解放入BeanDefinitionMap里，供后续使用");
        }
        if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            // 负责把autowired注解的bean注册到beanFactory中
            DemoBeanDefinition demoBeanDefinition = new DemoBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
            registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, demoBeanDefinition);
            System.out.println("把负责autowired的注解放入BeanDefinitionMap里，供后续使用");
        }

        if (!registry.containsBeanDefinition(LOGGER_REGISTER)) {
            // 负责把autowired注解的bean注册到beanFactory中
            DemoBeanDefinition demoBeanDefinition = new DemoBeanDefinition(LoggerRegister.class);
            registry.registerBeanDefinition(LOGGER_REGISTER, demoBeanDefinition);
            System.out.println("把负责注册logger的register放入BeanDefinitionMap里，供后续使用");
        }
    }

}
