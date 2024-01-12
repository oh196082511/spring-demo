package org.example;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对标spring的AutowiredAnnotationBeanPostProcessor
 */
public class AutowiredAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, SmartInstantiationAwareBeanPostProcessor {

    // 保存每个bean的依赖注入信息
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);

    @Override
    public void postProcessMergedBeanDefinition(Class<?> beanType, String beanName) {
        // 获取bean的依赖注入信息
        findAutowiringMetadata(beanName, beanType);
        System.out.println("AutowiredAnnotationBeanPostProcessor成功将beanName为 " + beanName + " 的bean的依赖注入信息保存到了injectionMetadataCache");
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(beanName);
        if (metadata != null) {
            return metadata;
        }
        // 把需要autowired的属性封装成InjectionMetadata
        metadata = buildAutowiringMetadata(clazz);
        this.injectionMetadataCache.put(beanName, metadata);
        return metadata;
    }

    private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();

        Field[] declaredFields = getDeclaredFields(clazz);
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                elements.add(new InjectionMetadata.InjectedElement(field));
            }


        }
        return new InjectionMetadata(clazz, elements);
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    @Override
    public void postProcessProperties(DemoApplicationContext applicationContext, Object bean, String beanName) {
        // 拿到所有变量，如果有autowired注解，就注入
        InjectionMetadata injectionMetadata = injectionMetadataCache.get(beanName);
        if (injectionMetadata == null) {
            return;
        }
        System.out.println("AutowiredAnnotationBeanPostProcessor开始注入beanName为 " + beanName + " 的bean的依赖注入信息");
        injectionMetadata.getInjectedElements().forEach(element -> {
            Field field = (Field) element.member;
            try {
                field.setAccessible(true);
                String[] beanNames = applicationContext.getBeanNamesForType(field.getType());
                if (beanNames.length != 1) {
                    throw new RuntimeException(String.format("%s的%s变量需要一个bean，但是找到了%d个", beanName, field.getName(), beanNames.length));
                }
                System.out.println("开始为 " + beanName + " 的 " + field.getName() + " 变量注入 " + beanNames[0]);
                field.set(bean, applicationContext.getBean(beanNames[0], field.getType()));
                System.out.println(beanName + " 的 " + field.getName() + " 变量注入 " + beanNames[0] + " 完成");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
