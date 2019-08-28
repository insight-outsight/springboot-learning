package org.springbootlearning.api.dao;



import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class BaseDaoTest {

    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    protected <T> void printListItem(List<T> resultList) {
        System.out.println(resultList.size());
        for(int i=0;i<resultList.size();i++){
            System.out.println(i+"."+resultList.get(i));
        }
    }
    
}
