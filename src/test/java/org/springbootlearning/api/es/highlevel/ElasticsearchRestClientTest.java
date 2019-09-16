package org.springbootlearning.api.es.highlevel;

import java.util.UUID;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.service.es.ElasticsearchService;
import org.springbootlearning.api.service.es.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class ElasticsearchRestClientTest {

    @Autowired
    private ElasticsearchService<Weather> elasticsearchService;
    
    @Test
    public void insertOrUpdateOne() {
        Weather entity = new Weather();
        entity.setSId(1113);
        entity.setBizId(133);
        entity.setBizType(13);
        entity.setOId("xxx");
        entity.setMTime(System.currentTimeMillis());
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.insertOrUpdateOne("weather", entity));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }

    @Test
    public void insertOrUpdateCircular() {
        Weather entity = new Weather();
        entity.setSId(1113);
        entity.setBizId(13);
        entity.setBizType(13);
        entity.setMTime(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            entity.setOId(UUID.randomUUID().toString());
            long st = System.currentTimeMillis();
            System.out.println(elasticsearchService.insertOrUpdateOne("weather", entity));
            System.out.println("cost: "+(System.currentTimeMillis()-st));
        }
    }
    
//    public void insertBatch(String index, List<EsEntity> list) {
//
//    }
//    
//    public <T> void deleteBatch(String index, Collection<T> idList) {
//
//    }
//    
//    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
//
//    }
//    public void deleteByQuery(String index, QueryBuilder builder) {
//
//    }
    
}
