package spring.with.nacos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-14 19:39
 **/
public class ImportPropertiesBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware,
        BeanFactoryAware {

    private final static Logger logger= LoggerFactory.getLogger(ImportPropertiesBeanDefinitionRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        if (environment instanceof ConfigurableEnvironment){
            MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
            String[] activeProfiles = environment.getActiveProfiles();
            if (Objects.nonNull(activeProfiles)){
                String propertyName = null;
                try {
                    propertyName=String.format("application-%s.properties", activeProfiles[0]);
                    propertySources.addLast(new ResourcePropertySource(propertyName));
                } catch (IOException e) {
                    logger.warn(propertyName+" not found", e);
                }
            }
            System.out.println(Arrays.toString(activeProfiles));
        }
    }
}
