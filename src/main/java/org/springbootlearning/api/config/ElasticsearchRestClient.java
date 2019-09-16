package org.springbootlearning.api.config;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientConfigurations
 * 中已经存在，
 * 只需在application.properties中配置
 * spring.elasticsearch.rest.username
 * spring.elasticsearch.rest.password
 * spring.elasticsearch.rest.uris
 * 即可
 * @author xuzhengchao
 *
 */
//@Configuration
public class ElasticsearchRestClient {
    
    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";
    
    @Value("${elasticsearch.high-level.client.nodes}")
    String[] ipAddress;

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient highLevelClient () {
        System.err.println(ipAddress);
        HttpHost[] hosts = Arrays.stream(ipAddress)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        RestClientBuilder restClientBuilder = RestClient.builder(hosts);
        return new RestHighLevelClient(restClientBuilder);
    }
    
    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            System.err.println(ip+"+"+port);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            throw new RuntimeException("ES config address format Error");
        }
    }

}