package org.example;

/**
 * 对标spring的AnnotationConfigUtils
 */
public class AnnotationConfigUtils {

    /**
     * The bean name of the internally managed Configuration annotation processor.
     */
    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";

    /**
     * 把相关的postProcessors注册到registry中
     * @param registry the registry to operate on
     */
    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            // 负责把component注解的bean注册到beanFactory中
            DemoBeanDefinition demoBeanDefinition = new DemoBeanDefinition(ConfigurationClassPostProcessor.class);
            registry.registerBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, demoBeanDefinition);
        }
    }

}
