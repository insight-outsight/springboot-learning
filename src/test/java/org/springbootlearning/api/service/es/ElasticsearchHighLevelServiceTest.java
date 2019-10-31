package org.springbootlearning.api.service.es;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.service.BaseServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class ElasticsearchHighLevelServiceTest extends BaseServiceTest{

    @Autowired
    private ElasticsearchHighLevelService<Counter> elasticsearchService;
    
    @Test
    public void testInsertOrUpdateOne() {
        Long s_id = 20003L;
        String o_id = "bfaknjbdfnjvjn";
        String id = geneId(s_id,o_id);
        Counter entity = new Counter();
        entity.setId(id);
        entity.setRoutingKey(s_id+"");
        entity.setSId(s_id);
        entity.setSType(3);
        entity.setBizId(220);
        entity.setBizType(10);
        entity.setOId(o_id);
        entity.setStatus(1);
        entity.setMTime(System.currentTimeMillis());
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.insertOrUpdateOne("test002", entity));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    @Test
    public void testInsertOrUpdateOne2() {
        Long s_id = 20004L;
        String o_id = "bfaknjbdfnjvjn";
        String id = geneId(s_id,o_id);
        Counter entity = new Counter();
        entity.setId(id);
        entity.setRoutingKey(s_id+"");
        entity.setSId(s_id);
        entity.setSType(3);
        entity.setOId(o_id);
        entity.setMTime(System.currentTimeMillis());
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.insertOrUpdateOne("test001", entity));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    @Test
    public void testGetById() throws Exception {
        Long s_id = 20003L;
        String o_id = "bfaknjbdfnjvjn";
        String id = geneId(s_id,o_id);
        Counter entity = new Counter();
        entity.setId(id);
        entity.setRoutingKey(s_id+"");
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.getById("test001", entity, Counter.class));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }

    private String geneId(Long s_id, String o_id) {
        String id = DigestUtils.sha1Hex(s_id+"_"+o_id);
        System.out.println("generated id="+id);
        return id;
    }
    
    @Test
    public void testSearchBySidAndOid() throws Exception {
        Long sId = 2001L;
        String oId = "abcdef";
//        String id = geneId(sId,oId);
        Counter entity = new Counter();
        entity.setRoutingKey(sId+"");
        entity.setSId(sId);
        entity.setOId(oId);
        long st = System.currentTimeMillis();
        List<String> fields = Lists.newArrayList();
        fields.add("sId");
        fields.add("oId");
        printListItem(elasticsearchService.searchByEntityFields("counter", entity, fields, Counter.class));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    
    @Test
    public void testExistsIndex() throws Exception {
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.existsIndex("test001"));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    @Test
    public void testCreateIndex() throws Exception {
        /**
         * {
          "settings": {
              "number_of_shards":5,
              "number_of_replicas":1
          },
          "mappings": {
              "properties": {
                "s_id": {
                  "type": "long"
                },
                "s_type": {
                  "type": "short",
                  "index": false
                },
                "o_id":{
                  "type": "keyword"
                },
                "m_time": {
                  "type": "date",
                  "index": false
                }
              }
          }
        }
         */
      Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>();
      
      Map<String, Object> propertySId = new HashMap<String, Object>();
      propertySId.put("type", "long");
      propertySId.put("index",true);
      properties.put("s_id",propertySId);
      
      Map<String, Object> propertySType = new HashMap<String, Object>();
      propertySType.put("type", "short"); 
      propertySType.put("index",false);
      properties.put("s_type",propertySType);
      
      Map<String, Object> propertyOId = new HashMap<String, Object>();
      propertyOId.put("type", "keyword");
      propertyOId.put("index",true);
      properties.put("o_id",propertyOId);
      
      Map<String, Object> propertyMTime = new HashMap<String, Object>();
      propertyMTime.put("type", "date"); 
      propertyMTime.put("index",false);
      propertyMTime.put("mtime",propertyMTime);
    
      long st = System.currentTimeMillis();
    
      System.out.println(elasticsearchService.
                createIndex("test-0-01", properties, 2, 1));
      System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    
    @Test
    public void testDeleteIndex() throws Exception {
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.deleteIndex("edex_5-5"));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
//    @Test
//    public void insertOrUpdateCircular() {
//        Counter entity = new Counter();
//        entity.setSId(1113L);
//        entity.setBizId(13);
//        entity.setBizType(13);
//        entity.setMTime(System.currentTimeMillis());
//        for (int i = 0; i < 10; i++) {
//            entity.setOId(UUID.randomUUID().toString());
//            long st = System.currentTimeMillis();
//            System.out.println(elasticsearchService.insertOrUpdateOne("weather", entity));
//            System.out.println("cost: "+(System.currentTimeMillis()-st));
//        }
//    }
    
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
