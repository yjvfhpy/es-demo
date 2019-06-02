package com.majie.elasticsearch;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;


/*
* 权威指南示例
*
* */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GuideDemo {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    public static void main(String[] args) {
        SpringApplication.run(GuideDemo.class, args);
    }

    @Test
    public void query() {

    }

}
