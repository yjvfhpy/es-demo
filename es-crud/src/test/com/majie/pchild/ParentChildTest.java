package com.majie.pchild;

import com.majie.ElasticsearchTemplateTest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ParentChildTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchTemplateTest.class, args);

    }


    /**
     * 不带子级查询
     */
    @Test
    public void queryParentByChild() {

        // hasChildQuery(String type, QueryBuilder query, ScoreMode scoreMode)
        QueryBuilder queryBuilder = JoinQueryBuilders.hasChildQuery("employee",
                QueryBuilders.matchQuery("hobby", "hiking"),
                ScoreMode.None);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        elasticsearchTemplate.queryForIds(searchQuery).forEach(id -> System.out.println(id));


        //no result？
        List<Employee> datas = elasticsearchTemplate.queryForList(searchQuery, Employee.class);

        System.out.println(datas.size());
        datas.forEach(o -> {
            System.out.println(o);
        });


    }


}