package org.springbootlearning.api.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebMvcCustomConfig extends WebMvcConfigurationSupport {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // setUseSuffixPatternMatch 后缀模式匹配
        configurer.setUseSuffixPatternMatch(true);
        // setUseTrailingSlashMatch 自动后缀路径模式匹配
        // configurer.setUseTrailingSlashMatch(true);
    }

    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(
            DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> servletRegistration = new ServletRegistrationBean<DispatcherServlet>(
                dispatcherServlet);
        servletRegistration.addUrlMappings("*.do");
        servletRegistration.addUrlMappings("*.html");
        return servletRegistration;
    }
    
    /**
     * <bean id="mappingJacksonHttpMessageConverter"
        class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
        <property name="supportedMediaTypes">
            <list>
                <value>application/json;charset=UTF-8</value>
            </list>
        </property>
       </bean>
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // 设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        // 设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }
    
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(getMappingJackson2HttpMessageConverter());
    }


    
}