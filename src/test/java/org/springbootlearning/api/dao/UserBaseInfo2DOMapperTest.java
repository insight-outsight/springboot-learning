/**
 * 
 */
package org.springbootlearning.api.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.ootb.espresso.facilities.IDGenerator;
import org.springbootlearning.api.dao.entity.UserBaseInfoDO;
import org.springbootlearning.api.dao.mapper.UserBaseInfo2DOMapper;
import org.springbootlearning.api.dao.mapper.UserBaseInfoDOMapper;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author admin
 *
 */
public class UserBaseInfo2DOMapperTest extends BaseDaoTest{

    
    @Autowired
    private UserBaseInfo2DOMapper userBaseInfoDOMapper;

    @Test
    public void testSelectByPrimaryKey() {
        long st = System.currentTimeMillis();
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByPrimaryKey(13000L);
        System.out.println("cost: "+(System.currentTimeMillis()-st));
        st = System.currentTimeMillis();
        userBaseInfoDO = userBaseInfoDOMapper.selectByPrimaryKey(46309L);
        System.out.println("cost: "+(System.currentTimeMillis()-st));
        st = System.currentTimeMillis();
        userBaseInfoDO = userBaseInfoDOMapper.selectByPrimaryKey(29703L);
        System.out.println("cost: "+(System.currentTimeMillis()-st));
        System.out.println(userBaseInfoDO);
    }
    
    @Test
    public void testSelectByUserName() {
        UserBaseInfoDO userBaseInfoDO = userBaseInfoDOMapper.selectByUserName("arch003@gmail.com");
        System.out.println(userBaseInfoDO);
    }


    @Test
    public void testInsert() {
        UserBaseInfoDO record = new UserBaseInfoDO();
        record.setCreateTime(new Date());
        record.setGender(1);
        record.setLastModifyTime(new Date());
        record.setNickName("test1467-ny");

        record.setUserId(IDGenerator.next());
        record.setUserName("arch1467@gmail.com");
        record.setUserNameType(1);
        
        System.out.println(record);
        int effectedRow = userBaseInfoDOMapper.insert(record);
        System.out.println(effectedRow);
        assertEquals(1,effectedRow);
    }
    
    @Test
    public void testInsertCircular() {
        
        UserBaseInfoDO record = new UserBaseInfoDO();
        record.setCreateTime(new Date());
        record.setGender(1);
        record.setLastModifyTime(new Date());

        record.setUserNameType(1);
        long st = System.currentTimeMillis();
        int count = 10000;
        for(int i=0;i<count;i++) {
            String suffix = "1468-"+i;
            record.setNickName("test"+suffix);
            record.setUserId(st+i);
            record.setUserName("arch"+suffix+"@gmail.com");
            int effectedRow = userBaseInfoDOMapper.insert(record);
            assertEquals(1,effectedRow);
        }
        long cost = System.currentTimeMillis() - st ;
        double avgCost = cost/count;
        System.out.println("test finished, cost:" + cost +", avgCost:" + avgCost);
        
    }
    
    
    @Test
    public void testInsertCircularMultiThread() throws Exception {
        
        int nThreads = 16;
        ExecutorService es = Executors.newFixedThreadPool(nThreads);
        
        for(final AtomicInteger ai = new AtomicInteger();ai.get()<nThreads;ai.incrementAndGet()) {
            final int ThreadNum = ai.get();
            es.execute(new Runnable() {

                @Override
                public void run() {
                    
                    UserBaseInfoDO record = new UserBaseInfoDO();
                    record.setCreateTime(new Date());
                    record.setGender(1);
                    record.setLastModifyTime(new Date());

                    record.setUserNameType(1);
                    long st = System.currentTimeMillis();
                    int count = 100000;
                    
                    AtomicLong seq = new AtomicLong(ThreadNum*1000*count);
                    for(int j=0;j<count;j++) {
                        String suffix = "1468-"+ThreadNum+"-"+j;
                        record.setNickName("test"+suffix);
                        record.setUserId(st+seq.getAndIncrement());
                        record.setUserName("arch"+suffix+"@gmail.com");
                        int effectedRow = userBaseInfoDOMapper.insert(record);
                        assertEquals(1,effectedRow);
                    }
                    long cost = System.currentTimeMillis() - st ;
                    double avgCost = cost/count;
                    System.out.println(ThreadNum+" test finished, cost:" + cost +", avgCost:" + avgCost);
                    
                    
                }
                
            });
        }
        
        try {
            es.shutdown();
            es.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
