package org.springbootlearning.api.dao;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.ootb.espresso.facilities.JacksonJSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class SpringBootDefaultDataSourceTest extends BaseDaoTest {
    
   @Autowired
   DataSourceProperties dataSourceProperties;

   @Autowired
   DataSource dataSource;
   
   @Autowired
   ApplicationContext applicationContext;
   
   @Autowired
   JdbcTemplate jdbcTemplate;

   @Test
   public void readConfigAndRunAQuery() {
       System.out.println("------------->");
       String[] beanNames = applicationContext.getBeanDefinitionNames();
       Arrays.sort(beanNames);
       for (String beanName : beanNames) {
           if(beanName.contains("SessionFactory"))
           System.out.println(beanName);
       }
       System.out.println("<-------------");

      // 获取配置的数据源
      DataSource dataSourceC = applicationContext.getBean(DataSource.class);
      // 查看配置数据源信息
      System.out.println(dataSourceC);
      System.out.println(dataSourceC.getClass().getName());
      System.out.println("is same :"+(dataSourceC == dataSource));
      System.out.println("Datasource Url:"+dataSourceProperties.getUrl());
      System.out.println("Datasource DriverClassName:"+dataSourceProperties.getDriverClassName());
      System.out.println("Datasource UserName:"+dataSourceProperties.getDataUsername());
      //执行SQL,输出查到的数据
      List<?> resultList = jdbcTemplate.queryForList("select id,user_id,user_name,create_time from t_arch_user_base_info limit 3;");
      System.out.println("===>>>>>>>>>>>\n" + JacksonJSONUtils.toJSON(resultList));
      JdbcTemplate jdbcTemplateC = new JdbcTemplate(dataSource);
      resultList = jdbcTemplateC.queryForList("select id,user_id,user_name,create_time from t_arch_user_base_info limit 2;");
      System.out.println("===>>>>>>>>>>>\n" + JacksonJSONUtils.toJSON(resultList));
   }
   
}