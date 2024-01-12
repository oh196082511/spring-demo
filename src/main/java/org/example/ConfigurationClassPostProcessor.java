package org.example;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 对标spring的ConfigurationClassPostProcessor
 * 负责将注解的类放入BeanDefinitionMap中
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final String BASE_PACKAGE = "org";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("ConfigurationClassPostProcessor负责将所有component注解的类放入BeanDefinitionMap中");
        // 负责将注解的类放入BeanDefinitionMap中
        ClassLoader contextClassLoader =  Thread.currentThread().getContextClassLoader();
        try {
            URL resource = contextClassLoader.getResource(BASE_PACKAGE);
            File file = new File(resource.toURI());
            List<Class> classes = new ArrayList<>();
            dfsLoadClass(file, classes);
            classes.stream()
                    .map(DemoBeanDefinition::new)
                    .forEach(demoBeanDefinition -> registry.registerBeanDefinition(demoBeanDefinition.getBeanClass().getName(), demoBeanDefinition));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("所有component注解的类均已放入BeanDefinitionMap中，完成ConfigurationClassPostProcessor的工作");
    }

    private void dfsLoadClass(File file, List<Class> classToAdd) {
        if (file.getName().endsWith(".class")) {
            try {
                String path = file.getPath();
                int pos = path.indexOf(BASE_PACKAGE);
                String targetName = path.substring(pos, path.length() - 6).replace("\\", ".");
                Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(targetName);
                Component annotation = aClass.getAnnotation(Component.class);
                if (annotation != null) {
                    classToAdd.add(aClass);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File subFile : files) {
                dfsLoadClass(subFile, classToAdd);
            }
        }
    }
}
