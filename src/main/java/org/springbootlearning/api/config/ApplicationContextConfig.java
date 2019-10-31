package org.springbootlearning.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextConfig {
    
    @Autowired
    private ConfigurableApplicationContext context;
    
    public ConfigurableApplicationContext getContext() {
        return context;
    }

    public int getCount() {
        return context.getBeanDefinitionCount();
    }

    public String[] getBeanDefinitionName() {
        return context.getBeanDefinitionNames();
    }
    
    public <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }
    
}
