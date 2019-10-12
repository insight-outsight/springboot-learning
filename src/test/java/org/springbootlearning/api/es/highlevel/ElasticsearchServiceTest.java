package org.springbootlearning.api.es.highlevel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springbootlearning.api.service.es.ElasticsearchService;
import org.springbootlearning.api.service.BaseServiceTest;
import org.springbootlearning.api.service.es.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class ElasticsearchServiceTest extends BaseServiceTest{

    @Autowired
    private ElasticsearchService<Counter> elasticsearchService;
    
    @Test
    public void testInsertOrUpdateOne() {
        Long s_id = 2002L;
        String o_id = "breiriefewgdfer";
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
        System.out.println(elasticsearchService.insertOrUpdateOne("counter", entity));
        System.out.println("cost: "+(System.currentTimeMillis()-st));
    }
    
    @Test
    public void testGetById() throws Exception {
        Long s_id = 2002L;
        String o_id = "breiriefewgdfer";
        String id = geneId(s_id,o_id);
        Counter entity = new Counter();
        entity.setId(id);
        entity.setRoutingKey(s_id+"");
        long st = System.currentTimeMillis();
        System.out.println(elasticsearchService.getById("counter", entity, Counter.class));
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
