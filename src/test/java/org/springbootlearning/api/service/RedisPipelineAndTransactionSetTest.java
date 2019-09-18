package org.springbootlearning.api.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

/**
 *          Transaction Pipeline    耗时（s）
    测试一：    N           N       64
    测试二：    N           Y       7
    测试三：    Y           Y       8
    测试四：    Y           N       8
 * @author xuzhengchao
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest({"spring.profiles.active=dev"})
public class RedisPipelineAndTransactionSetTest {

    @Autowired
    private JedisPool pool;

    private long rowCount = 1000000; // 100万

    @Test
    public void pipelineWithoutTransaction() {
        long start = System.currentTimeMillis();

        try {
            Jedis jedis = pool.getResource();
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < rowCount; i++) {
                if(i > 0 && i%10 == 0) {
                    p.sync();
                    String key = RandomStringUtils.randomAlphabetic(8);
                    p.set(key, RandomStringUtils.randomNumeric(5));
                    p.expire(key, 2*60);
                }
                else {
                    String key = RandomStringUtils.randomAlphabetic(8);
                    p.set(key, RandomStringUtils.randomNumeric(5));
                    p.expire(key, 2*60);
                }
            }
            jedis.close();

        } catch (Exception e) {
            e.printStackTrace();
            pool.close();
        }
        System.out.println("pipelineWithoutTransaction use " + (System.currentTimeMillis() - start) / 1000);

    }
    
    @Test
    public void pipelineWithTransaction() {
        long start = System.currentTimeMillis();

        try {
            Jedis jedis = pool.getResource();
            Pipeline p = jedis.pipelined();
            p.multi();
            for (int i = 0; i < rowCount; i++) {
                if(i > 0 && i%10 == 0) {
                    p.exec();
                    p.sync();
                    p.multi();
                    String key = RandomStringUtils.randomAlphabetic(8);
                    p.set(key, RandomStringUtils.randomNumeric(5));
                    p.expire(key, 2 * 60);
                }
                else {
                    String key = RandomStringUtils.randomAlphabetic(8);
                    p.set(key, RandomStringUtils.randomNumeric(5));
                    p.expire(key, 2 * 60);
                }
            }
            p.exec();
            p.sync();
            jedis.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            pool.close();
        }
        System.out.println("pipelineWithTransaction use " + (System.currentTimeMillis() - start) / 1000);

    }
    
    @Test
    public void noTransactionNoPipeline() {
        long start = System.currentTimeMillis();

        Jedis jedis = pool.getResource();
        try {
            for (int i = 0; i < rowCount; i++) {
                String key = RandomStringUtils.randomAlphabetic(8);
                jedis.set(key, RandomStringUtils.randomNumeric(5));
                jedis.expire(key, 2 * 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pool.close();
        }
        System.out.println("noTransactionNoPipeline use " + (System.currentTimeMillis() - start) / 1000);

    }
    @Test
    public void transactionNoPipeline() {
        long start = System.currentTimeMillis();

        try {
            Jedis jedis = pool.getResource();
            Transaction tx = jedis.multi();
            for (int i = 0; i < rowCount; i++) {
                if(i > 0 && i%10 == 0) {
                    tx.exec();
                    tx.close();
                    tx = jedis.multi();
                    String key = RandomStringUtils.randomAlphabetic(8);
                    tx.set(key, RandomStringUtils.randomNumeric(5));
                    tx.expire(key, 2 * 60);
                }
                else {
                    String key = RandomStringUtils.randomAlphabetic(8);
                    tx.set(key, RandomStringUtils.randomNumeric(5));
                    tx.expire(key, 2 * 60);
                }
            }
            tx.exec();
            jedis.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            pool.close();
        }
        System.out.println("transactionNoPipeline use " + (System.currentTimeMillis() - start) / 1000);

    }
}
