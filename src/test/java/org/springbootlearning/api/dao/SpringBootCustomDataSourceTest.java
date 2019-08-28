package org.springbootlearning.api.dao;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.ootb.espresso.facilities.JacksonJSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class SpringBootCustomDataSourceTest extends BaseDaoTest {

   @Qualifier("custom007DataSource")
   @Autowired
   DataSource dataSource;
   
   @Autowired
   ApplicationContext applicationContext;
   
   @Autowired
   JdbcTemplate jdbcTemplate;
   
   @Qualifier("custom007JdbcTemplate")
   @Autowired
   JdbcTemplate custom007JdbcTemplate;

   @Test
   public void readConfigAndRunAQuery() {
      DataSource dataSourceC = applicationContext.getBean(DataSource.class);

      System.out.println(dataSourceC);
      System.out.println(dataSourceC.getClass().getName());
      System.out.println("is same :"+(dataSourceC == dataSource));

      List<?> resultList = jdbcTemplate.queryForList("select id,user_id,user_name,create_time from t_arch_user_base_info limit 3;");
      System.out.println("===>>>>>>>>>>>\n" + JacksonJSONUtils.toJSON(resultList));
      resultList = custom007JdbcTemplate.queryForList("select id,user_id,user_name,create_time from user_base_info limit 3;");
      System.out.println("===>>>>>>>>>>>\n" + JacksonJSONUtils.toJSON(resultList));
      JdbcTemplate jdbcTemplateC = new JdbcTemplate(dataSource);
      resultList = jdbcTemplateC.queryForList("select id,user_id,user_name,create_time from user_base_info limit 2;");
      System.out.println("===>>>>>>>>>>>\n" + JacksonJSONUtils.toJSON(resultList));
   }
   
}