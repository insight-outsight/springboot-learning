package org.springbootlearning.api;

import org.mybatis.spring.annotation.MapperScan;
import org.ootb.espresso.facilities.DateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

//@EnableTransactionManagement
@SpringBootApplication
@MapperScan(basePackages = "org.springbootlearning.api.dao.mapper")
//@EnableApolloConfig//({"application.properties", "contentcenter.mysql-dev.properties"})
@ComponentScan(basePackages = {"org.springbootlearning.api"},
includeFilters = @Filter(type = FilterType.REGEX, pattern = "org\\.springbootlearning\\.api\\.service\\..*ServiceImpl"),
//excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = {Repository.class}),

//excludeFilters = {      //在扫描的包下，排除哪些
//  @Filter(type = FilterType.ANNOTATION , //过滤类型,注解类型
//      classes = {     //确定过滤的类
//          Repository.class
//      }
//  )
//},
//includeFilters = {      //在扫描的包下，包含哪些
//  @Filter(type = FilterType.ANNOTATION, //过滤类型
//      classes = {     //确定过滤的类
//          Controller.class
//      }
//  )
//}
//includeFilters = @Filter(type = FilterType.CUSTOM, classes = {FilterCustom.class}),
useDefaultFilters = true //使用includeFilters时，要将useDefaultFilters设置为false，否则会扫描并添加默认组件
)
public class TemplateApplicationBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplicationBootStrap.class, args);
        System.out.println("springbootlearing api started at ["+DateUtils.formatToStandardTimeStringNow()+"]");
    }
    
}
