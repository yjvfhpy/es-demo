package com.majie;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PushLiu {
    public static void main(String[] args) {

        SpringApplication.run(ElasticsearchTemplateTest.class, args);
    }


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Test
    public void pushSqu() {

        long rank = 1527068666;

        //组合查询

        //外层的过滤查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();


        //大于上次的时间
        queryBuilder.must(QueryBuilders.rangeQuery("rank").gt(rank));
        queryBuilder.must(QueryBuilders.rangeQuery("rank").lt(System.currentTimeMillis()));
        BoolQueryBuilder childQuery = QueryBuilders.boolQuery();


        //必须匹配type=topic
        childQuery.must(QueryBuilders.termQuery("type", "post"));

        childQuery.must(QueryBuilders.termsQuery("region", "1"));

        Collection<Integer> blockPublishers = null;
        if (null != blockPublishers && !blockPublishers.isEmpty()) {
            //黑名单过滤
            childQuery.mustNot(QueryBuilders.termsQuery("publisher", blockPublishers));
        }

        List<Integer> filterBlockLevels = Arrays.asList(1, 2);

        Long publisherId = 100000862l;
        boolean filterExcludePublisher = true;
        //需要过滤屏蔽级别的数据
        if (null != filterBlockLevels && !filterBlockLevels.isEmpty()) {
            //如果希望发布者自身能看见
            if (filterExcludePublisher && publisherId != null) {
                childQuery.minimumShouldMatch(1);
                childQuery.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("blockLevel", filterBlockLevels)));
                //不能过滤自己的
                childQuery.should(QueryBuilders.termQuery("publisher", publisherId));
            }
        }

        String curVerNumber = null;
        //按照版本号进行过滤
        if (null != curVerNumber) {
            //subject支持的最低版本号 不能高于当前的版本 （当前版本号要>=最低版本号）
            //放到must not里面 是可以兼顾null值的情况
            childQuery.mustNot(QueryBuilders.rangeQuery("support.version.minNumber").gt(curVerNumber));
        }


        //has_child层过滤查询
        HasChildQueryBuilder hasChildQueryBuilder = new HasChildQueryBuilder("subject", childQuery, ScoreMode.None);

        //设置innerHit这是同时返回父子数据的关键所在
        hasChildQueryBuilder.innerHit(new InnerHitBuilder());

        //设置has_child查询
        queryBuilder.must(hasChildQueryBuilder);

        //设置排序规则 --  按热度排序
        SortBuilder sortBuilder = null;

        boolean sortByScore = false;

        if (sortByScore) {
            sortBuilder = SortBuilders.fieldSort("score").order(SortOrder.DESC);
        } else {
            sortBuilder = SortBuilders.fieldSort("rank").order(SortOrder.DESC);
        }

        Client client = elasticsearchTemplate.getClient();
        SearchRequestBuilder searchBuidler = client.prepareSearch()
                .setIndices("feed")
                .setTypes("counter")
                .setFrom(0)
                .setSize(15)
                .setFetchSource(true)
                .setQuery(queryBuilder);


        boolean operatePosition = true;

        if (operatePosition) {
            searchBuidler.addSort(SortBuilders.fieldSort("squareOrder").order(SortOrder.ASC));
        }

        searchBuidler.addSort(sortBuilder);
        System.out.println(searchBuidler);

    }
}