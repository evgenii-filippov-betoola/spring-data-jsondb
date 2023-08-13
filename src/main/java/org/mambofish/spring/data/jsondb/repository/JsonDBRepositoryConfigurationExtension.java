package org.mambofish.spring.data.jsondb.repository;

import org.mambofish.spring.data.jsondb.rest.JsonDBMappingContextFactoryBean;
import org.springframework.beans.factory.aot.BeanRegistrationAotProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

import java.util.Collection;
import java.util.Collections;

/**
 * @author vince
 */
public class JsonDBRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    @Override
    protected String getModulePrefix() {
        return "json";
    }

    @Override
    public String getRepositoryFactoryBeanClassName() {
        return JsonDBRepositoryFactoryBean.class.getName();
    }

    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.<Class<?>>singleton(JsonDBRepository.class);
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
        builder.addPropertyReference("mappingContext", "jsonMappingContext");
    }

    @Override
    public String getModuleIdentifier() {
        return super.getModuleIdentifier();
    }

    @Override
    public Class<? extends BeanRegistrationAotProcessor> getRepositoryAotProcessor() {
        return super.getRepositoryAotProcessor();
    }

    @Override
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {

        super.registerBeansForRoot(registry, config);

        Object source = config.getSource();

        registerIfNotAlreadyRegistered(() -> new RootBeanDefinition(JsonDBMappingContextFactoryBean.class), registry,
                "jsonMappingContext", source);
    }
}
