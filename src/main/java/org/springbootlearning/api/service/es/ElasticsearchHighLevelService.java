package org.springbootlearning.api.service.es;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.ootb.espresso.facilities.JacksonJSONUtils;
import org.ootb.espresso.facilities.converter.UnderlineCamelcaseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import com.google.common.collect.Maps;

@Service
public class ElasticsearchHighLevelService<T extends ESEntity> {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchHighLevelService.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public boolean existsIndex(String index) throws Exception {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    public boolean createIndex(String index, Map<String, Map<String, Object>> properties, int numberOfShards,
            int numberOfReplicas) throws Exception {
        
        CreateIndexRequest request = new CreateIndexRequest(index);
//        request.settings(Settings.builder().put("index.number_of_shards", numberOfShards)
//                .put("index.number_of_replicas", numberOfReplicas));
//        request.mapping(index, XContentType.JSON);
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
                "biz_id": {
                  "type": "short",
                  "index": false
                },
                "biz_type":{
                  "type": "short"
                },
                "o_id":{
                  "type": "keyword"
                },
                "status":{
                  "type": "short",
                  "index": false
                },
                "m_time": {
                  "type": "date",
                  "index": false
                }
              }
          }
        }
         */
//        Map<String, Object> properties = new HashMap<String, Object>();
//        Map<String, Object> property = new HashMap<String, Object>();
//        property.put("type", "text"); // 类型 
//        property.put("index",true);
//        property.put("analyzer", "ik_max_word"); // 分词器
//        properties.put("title",property);
        
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject()
                    .startObject("mappings")
//                        .startObject("_doc")
                            .field("properties", properties)
//                        .endObject()
                    .endObject()
                    .startObject("settings")
                        .field("number_of_shards", numberOfShards)
                        .field("number_of_replicas", numberOfReplicas)
                    .endObject()
                .endObject();

        request.source(builder);
        
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        logger.info(
                "create index response,index:{},numberOfShards:{},numberOfReplicas:{},Acknowledged:{},isShardsAcknowledged:{}",
                response.index(), numberOfShards, numberOfReplicas, response.isAcknowledged(),
                response.isShardsAcknowledged());
        return response.isAcknowledged();
    }

    public boolean deleteIndex(String index) throws Exception {
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        logger.info("delete index response,index:{},Acknowledged:{}",index, acknowledgedResponse.isAcknowledged());
        return acknowledgedResponse.isAcknowledged();
    }

    public boolean exists(String index, String id) throws IOException {
        /**
         * 
         * 执行GetRequest也可以以异步方式完成，以便客户端可以直接返回，用户需要通过将请求和侦听器传递给异步exists方法来指定响应或潜在故障的处理方式：
         * 
         * client.existsAsync(getRequest, RequestOptions.DEFAULT, listener);
         * 要执行的GetRequest和执行完成时要使用的ActionListener。
         * 异步方法不会阻塞并立即返回，完成后，如果执行成功完成，则使用onResponse方法回调ActionListener，如果失败则使用onFailure方法。
         * 
         * exists的典型侦听器如下所示：
         * 
         * ActionListener<Boolean> listener = new ActionListener<Boolean>() {
         * //执行成功完成时调用。
         * 
         * @Override public void onResponse(Boolean exists) {
         * 
         *           } //GetRequest失败时调用。
         * @Override public void onFailure(Exception e) {
         * 
         *           } };
         */
        GetRequest getRequest = new GetRequest(index, id);
        // 建议关闭获取_source和任何存储的字段，以便请求稍微轻一点：
        getRequest.fetchSourceContext(new FetchSourceContext(false)); // 禁用提取_source
        getRequest.storedFields("_none_");// 禁用提取存储的字段
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    // 通过指定 id，相当于PUT /_index/_doc/_id ...
    // 如果此 id 存在那么就是全量替换更新，否则是插入。
    public boolean insertOrUpdateOne(String index, T esEntity) {
        IndexRequest request = new IndexRequest(index);
        if (StringUtils.isNotBlank(esEntity.getId())) {
            request.id(esEntity.getId());
        }
        if (StringUtils.isNotBlank(esEntity.getRoutingKey())) {
            request.routing(esEntity.getRoutingKey());
        }
//        request.version(2);
//        request.versionType(VersionType.EXTERNAL);
//        request.opType(OpType.CREATE);//request.opType("create");作为String提供的操作类型：可以为create或update（默认）。
//        request.setIfPrimaryTerm(1).setIfSeqNo(2)
//        request.setPipeline("pipeline");//索引文档之前要执行的摄取管道的名称。

        // 当文档被索引时会从主shard将数据复制到复制shard, 主shard需要等待复制shard的响应后返回执
        // 行结果, 此等待时间默认为1min, 可以通过在请求中添加timeout修改此时间
        // 分片并不是随时可用的，当分片进行备份等操作时，是不能进行索引操作的。因此需要等待分片可用后，
        // 再进行操作。这时，就会出现一定的等待时间，如果超过等地时间则返回并抛出错误，这个等待时间可
        // 以通过timeout设置：
        request.timeout(TimeValue.timeValueMillis(3500));
//        request.type("_doc");
        request.setRefreshPolicy(RefreshPolicy.NONE);
        // 文档源以字符串形式提供。
        String jsonStr = JacksonJSONUtils.toJSON(esEntity);
        request.source(jsonStr, XContentType.JSON);
        logger.debug("es insert or update document jsonStr:{}", jsonStr);
        /**
         * 还可以以不同的方式提供文档源： Map<String, Object> jsonMap = new HashMap<>();
         * jsonMap.put("user", "kimchy"); jsonMap.put("postDate", new Date());
         * jsonMap.put("message", "trying out Elasticsearch"); IndexRequest indexRequest
         * = new IndexRequest("posts", "doc", "1") .source(jsonMap);
         * 文档源作为Map提供，可自动转换为JSON格式。 XContentBuilder builder =
         * XContentFactory.jsonBuilder(); builder.startObject(); { builder.field("user",
         * "kimchy"); builder.timeField("postDate", new Date());
         * builder.field("message", "trying out Elasticsearch"); } builder.endObject();
         * IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
         * .source(builder); 文档源作为XContentBuilder对象提供，Elasticsearch内置辅助生成JSON内容。
         * IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
         * .source("user", "kimchy", "postDate", new Date(), "message", "trying out
         * Elasticsearch"); 文档源作为Object键值对提供，转换为JSON格式。
         */
        try {
            IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            /**
             * 返回的IndexResponse允许检索有关已执行操作的信息，如下所示：
             * 
             * String index = indexResponse.getIndex(); String type =
             * indexResponse.getType(); String id = indexResponse.getId(); long version =
             * indexResponse.getVersion(); if (indexResponse.getResult() ==
             * DocWriteResponse.Result.CREATED) {
             * 
             * } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
             * 
             * } ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo(); if
             * (shardInfo.getTotal() != shardInfo.getSuccessful()) {
             * 
             * } if (shardInfo.getFailed() > 0) { for (ReplicationResponse.ShardInfo.Failure
             * failure : shardInfo.getFailures()) { String reason = failure.reason(); } }
             * 处理（如果需要）第一次创建文档的情况。 处理（如果需要）文档被重写的情况，因为它已经存在。 处理成功碎片数小于总碎片数的情况。 处理潜在的失败。
             * 如果存在版本冲突，则抛出ElasticsearchException：
             * 
             * IndexRequest request = new IndexRequest("posts", "doc", "1") .source("field",
             * "value") .version(1); try { IndexResponse response = client.index(request,
             * RequestOptions.DEFAULT); } catch(ElasticsearchException e) { if (e.status()
             * == RestStatus.CONFLICT) {
             * 
             * } } 引发的异常表示返回了版本冲突错误。 如果将opType设置为create并且已存在具有相同索引、类型和ID的文档，则会发生相同的情况：
             * 
             * IndexRequest request = new IndexRequest("posts", "doc", "1") .source("field",
             * "value") .opType(DocWriteRequest.OpType.CREATE); try { IndexResponse response
             * = client.index(request, RequestOptions.DEFAULT); }
             * catch(ElasticsearchException e) { if (e.status() == RestStatus.CONFLICT) {
             * 
             * } } 引发的异常表示返回了版本冲突错误。
             */
            if (StringUtils.isNotBlank(indexResponse.getId()) && indexResponse.getResult() != null) {
                // indexResponse.getResult().name() is CREATED or UPDATED
                logger.debug("es insert or update document id:{},result name:{}", indexResponse.getId(),
                        indexResponse.getResult().name());
                return true;
            } else {
                logger.error("es insert or update document return error");
                return false;
            }
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
        /**
         * 执行IndexRequest也可以以异步方式完成，以便客户端可以直接返回，用户需要通过将请求和侦听器传递给异步索引方法来指定响应或潜在故障的处理方式：
         * client.indexAsync(request, RequestOptions.DEFAULT, listener);
         * 要执行的IndexRequest和执行完成时要使用的ActionListener。
         * 异步方法不会阻塞并立即返回，一旦完成，如果执行成功完成，则使用onResponse方法回调ActionListener，如果失败则使用onFailure方法。
         * 
         * index的典型侦听器如下所示：
         * 
         * listener = new ActionListener<IndexResponse>() {
         * 
         * @Override public void onResponse(IndexResponse indexResponse) {
         * 
         *           }
         * 
         * @Override public void onFailure(Exception e) {
         * 
         *           } }; onResponse — 执行成功完成时调用。 onFailure — 当整个IndexRequest失败时调用。
         * 
         */
    }

//
    /**
     * List<EsEntity> list = new ArrayList<>(); books.forEach(item -> list.add(new
     * EsEntity<>(item.getId().toString(), item)));
     * 
     * @throws Exception
     */
    // 通过指定 id批量index，如果此 id 存在那么就是全量替换更新，否则是插入。
    public boolean insertOrUpdateBatch(String index, List<T> esEntityList) throws Exception {
        BulkRequest request = new BulkRequest();
        esEntityList.forEach(esEntity -> {
            IndexRequest indexRequest = new IndexRequest(index);
            if (StringUtils.isNotBlank(esEntity.getId())) {
                indexRequest.id(esEntity.getId());
            }
            if (StringUtils.isNotBlank(esEntity.getRoutingKey())) {
                indexRequest.routing(esEntity.getRoutingKey());
            }
            request.add(indexRequest.source(JacksonJSONUtils.toJSON(esEntity), XContentType.JSON));
        });
        request.timeout(TimeValue.timeValueMillis(3500));
        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if (bulkResponse != null) {
            logger.debug("es batch insert or update size,response size:{},took:{},hasFailures:{},failMessages:{}",
                    esEntityList.size(), bulkResponse.getItems().length, bulkResponse.getTook().toString(),
                    bulkResponse.hasFailures(), bulkResponse.buildFailureMessage());
            if (bulkResponse.hasFailures()) {
                logger.error("es batch insert or update document return error");
                return false;
            }
            BulkItemResponse[] bulkItemResponseItems = bulkResponse.getItems();
            if (bulkItemResponseItems.length != esEntityList.size()) {
                logger.error("es batch insert or update document reponse size:{} != request size:{}",
                        bulkResponse.getItems().length, esEntityList.size());
                return false;
            }
            boolean hasFailure = false;
            for (BulkItemResponse bulkItemResponse : bulkItemResponseItems) {
                if (bulkItemResponse.isFailed()) {
                    hasFailure = true;
                    logger.error(
                            "es batch insert or update document contain error,index:{},id:{},itemId:{},failMessage:{}",
                            bulkItemResponse.getIndex(), bulkItemResponse.getId(), bulkItemResponse.getItemId(),
                            bulkItemResponse.getFailureMessage());
                }
            }
            return !hasFailure;
        } else {
            logger.error("es batch insert or update document response null");
            return true;
        }
    }

    public T getById(String index, T esEntity, Class<T> cls) throws Exception {

        GetRequest request = new GetRequest(index, esEntity.getId());
        // 禁用源检索，默认情况下启用
//         request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

//         //为特定字段配置源包含
//         String[] includes = new String[]{"message", "*Date"};
//         String[] excludes = Strings.EMPTY_ARRAY;
//         FetchSourceContext fetchSourceContext =
//                 new FetchSourceContext(true, includes, excludes);
//         request.fetchSourceContext(fetchSourceContext);
//         
//         //为特定字段配置源排除
//         String[] includes = Strings.EMPTY_ARRAY;
//         String[] excludes = new String[]{"message"};
//         FetchSourceContext fetchSourceContext =
//                 new FetchSourceContext(true, includes, excludes);
//         request.fetchSourceContext(fetchSourceContext);

        if (StringUtils.isNotBlank(esEntity.getRoutingKey())) {
            request.routing(esEntity.getRoutingKey());
        }

//         偏好值。
//         request.preference("preference");
        // 配置特定存储字段的检索（要求字段分别存储在映射中）
        // 将realtime标志设置为false（默认为true）。
//         request.realtime(false);
//         在检索文档之前执行刷新（默认为false）。
//         request.refresh(true);
        // 版本。
//         request.version(2);
        // 版本类型。
//         request.versionType(VersionType.EXTERNAL);
//         request.storedFields("message"); 

        GetResponse getResponse = restHighLevelClient.get(request, RequestOptions.DEFAULT);

        logger.debug("get document index:{},id:{},exists:{}", getResponse.getIndex(), getResponse.getId(),
                getResponse.isExists());

        if (getResponse.isExists()) {
//             long version = getResponse.getVersion();
            String sourceAsString = getResponse.getSourceAsString();
            logger.debug("get document sourceAsString:{}", sourceAsString);
            T result = parseToEntityFromMap(getResponse.getSourceAsMap(), cls);
            result.setId(getResponse.getId());
            result.setRoutingKey(esEntity.getRoutingKey());
//             byte[] sourceAsBytes = getResponse.getSourceAsBytes();  
//             String message = getResponse.getField("message").getValue();
            return result;
        } else {
            return null;
        }

        /**
         * 以字符串形式检索文档。 将文档检索为Map<String, Object>。 以byte[]的形式检索文档。
         * 处理未找到文档的方案，请注意，虽然返回的响应具有404状态代码，但返回有效的GetResponse而不是抛出异常，此类响应不包含任何源文档，并且其isExists方法返回false。
         * 当针对不存在的索引执行get请求时，响应具有404状态代码，抛出ElasticsearchException，需要按如下方式处理：
         * 
         * GetRequest request = new GetRequest("does_not_exist", "doc", "1"); try {
         * GetResponse getResponse = client.get(request, RequestOptions.DEFAULT); }
         * catch (ElasticsearchException e) { if (e.status() == RestStatus.NOT_FOUND) {
         * 
         * } } 处理抛出的异常，因为索引不存在。 如果已请求特定文档版本，并且现有文档具有不同的版本号，则会引发版本冲突：
         * 
         * try { GetRequest request = new GetRequest("posts", "doc", "1").version(2);
         * GetResponse getResponse = client.get(request, RequestOptions.DEFAULT); }
         * catch (ElasticsearchException exception) { if (exception.status() ==
         * RestStatus.CONFLICT) {
         * 
         * } } 引发的异常表示返回了版本冲突错误。
         */

        /**
         * 执行GetRequest也可以以异步方式完成，以便客户端可以直接返回，用户需要通过将请求和侦听器传递给异步get方法来指定响应或潜在故障的处理方式：
         * 
         * client.getAsync(request, RequestOptions.DEFAULT, listener);
         * 要执行的GetRequest和执行完成时要使用的ActionListener。
         * 异步方法不会阻塞并立即返回，完成后，如果执行成功完成，则使用onResponse方法回调ActionListener，如果失败则使用onFailure方法。
         * 
         * get的典型监听器看起来像：
         * 
         * ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
         * 
         * @Override public void onResponse(GetResponse getResponse) {
         * 
         *           }
         * 
         * @Override public void onFailure(Exception e) {
         * 
         *           } }; onResponse — 执行成功完成时调用。 onFailure — 在整个GetRequest失败时调用。
         */

        /**
         * Get响应 返回的GetResponse允许检索所请求的文档及其元数据和最终存储的字段。
         */
    }

    private T parseToEntityFromMap(Map<String, Object> sourceAsMap, Class<T> cls) throws Exception {
        T result = cls.newInstance();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object mapFieldObject = sourceAsMap.get(UnderlineCamelcaseConverter.humpToLine2(f.getName()));
            try {
                if (mapFieldObject != null) {
                    if (f.getType() == Long.class) {
                        if (mapFieldObject.getClass() == Long.class || mapFieldObject.getClass() == Integer.class) {
                            f.set(result, Long.valueOf(mapFieldObject + ""));
                            logger.debug("fieldName:{},fieldClass:{},mapFieldObjectClass:{},mapFieldObject:{}",
                                    f.getName(), f.getType(), mapFieldObject.getClass(), mapFieldObject);
                        }
                    } else {
                        if (f.getType().isAssignableFrom(mapFieldObject.getClass())) {
                            f.set(result, mapFieldObject);
                            logger.debug("fieldName:{},fieldClass:{},mapFieldObjectClass:{},mapFieldObject:{}",
                                    f.getName(), f.getType(), mapFieldObject.getClass(), mapFieldObject);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
                throw e;
            }
        }
        return result;
    }

    public List<T> searchByEntityFields(String index, T esEntity, List<String> fieldNameList, Class<T> cls)
            throws Exception {
//        SearchRequest searchRequest = new SearchRequest("gdp_tops*");
        SearchRequest searchRequest = new SearchRequest(index);
        if (StringUtils.isNotBlank(esEntity.getRoutingKey())) {
            searchRequest.routing(esEntity.getRoutingKey());
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        Map<String, Object> queryMap = Maps.newHashMap();
//        queryMap.put("s_id", sId);
//        queryMap.put("o_id", oId);
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

        for (String fieldName : fieldNameList) {
            boolBuilder.must(QueryBuilders.matchQuery(UnderlineCamelcaseConverter.humpToLine2(fieldName),
                    getFieldValue(esEntity, esEntity.getClass(), fieldName)));
        }

        searchSourceBuilder.query(boolBuilder);
//        searchSourceBuilder.query(QueryBuilders.termQuery("city", "北京市"));
        searchSourceBuilder.timeout(new TimeValue(3, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            logger.debug("hits:{}", searchResponse.getHits().getHits().length);

            Arrays.stream(hits).forEach(i -> {
                logger.debug("search result index:{},sourceAsString:{}", i.getIndex(), i.getSourceAsString());
            });

            List<T> resultList = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                T result = parseToEntityFromMap(hit.getSourceAsMap(), cls);
                result.setId(hit.getId());
                result.setRoutingKey(esEntity.getRoutingKey());
                resultList.add(result);
            }
            return resultList;
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        }
    }

    private Object getFieldValue(T esEntity, Class<? extends ESEntity> cls, String fieldName) throws Exception {
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getName().equals(fieldName)) {
                try {
                    return f.get(esEntity);
                } catch (Exception e) {
                    logger.error("", e);
                    throw e;
                }
            }
        }
        return null;
    }

    /**
     * Integer pageIndex = 1; Integer pageSize = 5; String indexName = "demo";
     * Map<String, Object> data = new HashMap<>(); data.put("title", title);
     * 
     * List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
     * SearchRequest searchRequest = new SearchRequest(indexName); //
     * searchRequest.types(indexName); queryBuilder(pageIndex, pageSize, data,
     * indexName, searchRequest); try { SearchResponse response =
     * highLevelClient.search(searchRequest, RequestOptions.DEFAULT); for (SearchHit
     * hit : response.getHits().getHits()) { Map<String, Object> map =
     * hit.getSourceAsMap(); map.put("id", hit.getId()); result.add(map);
     * 
     * // 取高亮结果 Map<String, HighlightField> highlightFields =
     * hit.getHighlightFields(); HighlightField highlight =
     * highlightFields.get("title"); Text[] fragments = highlight.fragments(); //
     * 多值的字段会有多个值 String fragmentString = fragments[0].string();
     * System.out.println("高亮：" + fragmentString); } System.out.println("pageIndex:"
     * + pageIndex); System.out.println("pageSize:" + pageSize);
     * System.out.println(response.getHits().getTotalHits());
     * System.out.println(result.size()); for (Map<String, Object> map : result) {
     * System.out.println(map.get("title")); } } catch (IOException e) {
     * e.printStackTrace(); }
     * 
     * private void queryBuilder(Integer pageIndex, Integer pageSize, Map<String,
     * Object> query, String indexName, SearchRequest searchRequest) { if (query !=
     * null && !query.keySet().isEmpty()) { SearchSourceBuilder searchSourceBuilder
     * = new SearchSourceBuilder(); if (pageIndex != null && pageSize != null) {
     * searchSourceBuilder.size(pageSize); if (pageIndex <= 0) { pageIndex = 0; }
     * searchSourceBuilder.from((pageIndex - 1) * pageSize); } BoolQueryBuilder
     * boolBuilder = QueryBuilders.boolQuery(); query.keySet().forEach(key -> {
     * boolBuilder.must(QueryBuilders.matchQuery(key, query.get(key)));
     * 
     * }); searchSourceBuilder.query(boolBuilder);
     * 
     * HighlightBuilder highlightBuilder = new HighlightBuilder();
     * HighlightBuilder.Field highlightTitle = new
     * HighlightBuilder.Field("title").preTags("<strong>").postTags("</strong>");
     * highlightTitle.highlighterType("unified");
     * highlightBuilder.field(highlightTitle);
     * searchSourceBuilder.highlighter(highlightBuilder);
     * 
     * searchRequest.source(searchSourceBuilder); } }
     */

    /**
     * 
     * SearchRequest request = new SearchRequest("order"); //构造bool查询
     * BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); if
     * (!ObjectUtils.isEmpty(buyer)) {
     * boolQueryBuilder.must(QueryBuilders.matchQuery("buyer", buyer)); } if
     * (!ObjectUtils.isEmpty(status)) {
     * boolQueryBuilder.must(QueryBuilders.matchQuery("status", status)); }
     * //对应filter if (!ObjectUtils.isEmpty(gtePrice) &&
     * !ObjectUtils.isEmpty(ltePrice)) {
     * boolQueryBuilder.filter(QueryBuilders.rangeQuery("totalPrice").from(ltePrice).to(gtePrice));
     * }
     * 
     * SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); //排序
     * searchSourceBuilder.sort(SortBuilders.fieldSort("totalPrice").order(SortOrder.DESC));
     * //分页 searchSourceBuilder.from(index).size(size).query(boolQueryBuilder);
     * request.searchType(SearchType.DEFAULT).source(searchSourceBuilder);
     * List<Map<String, Object>> list = new ArrayList<>(); for (SearchHit s :
     * client.search(request, RequestOptions.DEFAULT).getHits().getHits()) {
     * list.add(s.getSourceAsMap()); } return list;
     * 
     * @throws Exception
     */
    public String update(String index, T esEntity) throws Exception {
        UpdateRequest request = new UpdateRequest(index, esEntity.getId());
        if (StringUtils.isNotBlank(esEntity.getRoutingKey())) {
            request.routing(esEntity.getRoutingKey());
        }
        Map<String, Object> temp = new HashMap<>();
        Field[] fields = esEntity.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.get(esEntity) != null) {
                    temp.put(f.getName(), f.get(esEntity));
                }
            } catch (Exception e) {
                logger.error("", e);
                throw e;
            }
        }
        request.doc(temp);
        return restHighLevelClient.update(request, RequestOptions.DEFAULT).status().name();
    }

    public String delete(String index, String id) {
        DeleteRequest request = new DeleteRequest(index, id);
//         request.routing("routing");
//         等待主碎片可用的作为TimeValue的超时。
//         request.timeout(TimeValue.timeValueMinutes(2)); 
//         request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//         request.version(2);
//         request.versionType(VersionType.EXTERNAL);

        try {
            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            /**
             * Delete响应 返回的DeleteResponse允许检索有关已执行操作的信息，如下所示：
             * 
             * String index = deleteResponse.getIndex(); String type =
             * deleteResponse.getType(); String id = deleteResponse.getId(); long version =
             * deleteResponse.getVersion(); ReplicationResponse.ShardInfo shardInfo =
             * deleteResponse.getShardInfo(); if (shardInfo.getTotal() !=
             * shardInfo.getSuccessful()) {
             * 
             * } if (shardInfo.getFailed() > 0) { for (ReplicationResponse.ShardInfo.Failure
             * failure : shardInfo.getFailures()) { String reason = failure.reason(); } }
             * 处理成功碎片数小于总分片数的情况。 处理潜在的失败。 还可以检查文档是否被找到：
             * 
             * DeleteRequest request = new DeleteRequest("posts", "doc", "does_not_exist");
             * DeleteResponse deleteResponse = client.delete( request,
             * RequestOptions.DEFAULT); if (deleteResponse.getResult() ==
             * DocWriteResponse.Result.NOT_FOUND) {
             * 
             * } 如果找不到要删除的文档，请执行某些操作。 如果存在版本冲突，则抛出ElasticsearchException：
             * 
             * try { DeleteResponse deleteResponse = client.delete( new
             * DeleteRequest("posts", "doc", "1").version(2), RequestOptions.DEFAULT); }
             * catch (ElasticsearchException exception) { if (exception.status() ==
             * RestStatus.CONFLICT) {
             * 
             * } } 引发的异常表示返回了版本冲突错误。
             */
            return response.status().name();
            /**
             * 执行DeleteRequest也可以以异步方式完成，以便客户端可以直接返回，用户需要通过将请求和侦听器传递给异步删除方法来指定响应或潜在故障的处理方式：
             * 
             * client.deleteAsync(request, RequestOptions.DEFAULT, listener);
             * 要执行的DeleteRequest和执行完成时要使用的ActionListener。
             * 异步方法不会阻塞并立即返回，完成后，如果执行成功完成，则使用onResponse方法回调ActionListener，如果失败则使用onFailure方法。
             * 
             * delete的典型侦听器如下所示：
             * 
             * listener = new ActionListener<DeleteResponse>() {
             * 
             * @Override public void onResponse(DeleteResponse deleteResponse) {
             * 
             *           }
             * 
             * @Override public void onFailure(Exception e) {
             * 
             *           } }; onResponse — 执行成功完成时调用。 onFailure — 在整个DeleteRequest失败时调用。
             */
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

//    public void deleteByQuery(String index, QueryBuilder builder) {
//        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
//        request.setQuery(builder);
//        //设置批量操作数量,最大为10000
//        request.setBatchSize(10000);
//        request.setConflicts("proceed");
//        try {
//            client.deleteByQuery(request, RequestOptions.DEFAULT);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//    public <T> void deleteBatch(String index, Collection<T> idList) {
//        BulkRequest request = new BulkRequest();
//        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
//        try {
//            client.bulk(request, RequestOptions.DEFAULT);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
