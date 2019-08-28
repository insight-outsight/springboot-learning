package org.springbootlearning.api.service;



import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class BaseServiceTest {

    protected <T> void printListItem(List<T> resultList) {
        System.out.println(resultList.size());
        for(int i=0;i<resultList.size();i++){
            System.out.println(i+"."+resultList.get(i));
        }
    }
    
}
