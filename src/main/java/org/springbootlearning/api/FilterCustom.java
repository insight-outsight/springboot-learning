package org.springbootlearning.api;

import java.io.IOException;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class FilterCustom implements TypeFilter {
 
    /**
     * 
     * @Title: match
     * @Description: 覆盖方法注释标签说明
     * @param metadataReader 读取的当前正在扫描类的信息
     * @param metadataReaderFactory 类工厂中其它类的信息
     * @return
     * @throws IOException
     * @see org.springframework.core.type.filter.TypeFilter#match(org.springframework.core.type.classreading.MetadataReader,
     *      org.springframework.core.type.classreading.MetadataReaderFactory)
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException {
        
        //获得当前正在扫描的类的注解信息
        //AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获得当前正在扫描的类的资源信息
        //Resource resource = metadataReader.getResource();
        //获得当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();

        // 获取当前类路径的信息
        // Resource resource = metadataReader.getResource();
        if (classMetadata.getClassName().endsWith("ServiceImpl")) {
            return true;
        }
        return false;
    }
 
}