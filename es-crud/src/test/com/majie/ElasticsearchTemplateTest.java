package com.majie;


import com.majie.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticsearchTemplateTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public static void main(String[] args) {

        SpringApplication.run(ElasticsearchTemplateTest.class, args);
    }

    /**
     * SearchQuery:生成查询
     * 查询所有文档 - match_all
     * <p>
     * GET /_search
     * {
     * "query": {
     * "match_all": {}
     * }
     * }
     **/
    @Test
    public void searchQuery() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery()).build();
        List<Product> datas = elasticsearchTemplate.queryForList(searchQuery, Product.class);
        datas.forEach(o -> System.out.println(o));
    }

    /**
     * 词语匹配 - match
     * 匹配查询接受文本/数字/日期，分析它们，并构造一个查询，它会将查询词分析后再去匹配。
     * <p>
     * GET /_search
     * {
     * "query": {
     * "match": {
     * "FIELD": "TEXT"
     * }
     * }
     * }
     **/
    @Test
    public void matchQueryTest() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", "gaolujie")).build();

        List<Product> datas = elasticsearchTemplate.queryForList(searchQuery, Product.class);
        datas.forEach(o -> System.out.println(o));
    }

    /**
     * 分页
     * 和query同级
     * <p>
     * {
     * "query": {
     * "match_all": {}
     * },
     * "from":1,   //从第几条开始    (从0开始)
     * "size":1      //大小
     * }
     **/
    @Test
    public void match_phrase() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(new PageRequest(0, 2))
                .build();

        elasticsearchTemplate.queryForList(searchQuery, Product.class).forEach(o -> {
            System.out.println(o);
        });
    }


    /**
     * 排序
     * <p>
     * 和query同级
     **/
    @Test
    public void sort() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("name", "yagao"))
                .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC)).build();

        elasticsearchTemplate.queryForList(searchQuery, Product.class).forEach(o -> {
            System.out.println(o);
        });
    }


    /**
     * 指定查询的字段
     * <p>
     * {
     * "_source": ["name","price"]   //结果只显示name和price
     * }
     **/
    @Test
    public void fetchSourceFilter() {
        String[] include = {"name", "price"};
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(include, null);   //两个参数分别是要显示的和不显示的

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withSourceFilter(fetchSourceFilter)
                .build();

        elasticsearchTemplate.queryForList(searchQuery, Product.class)
                .forEach(o -> System.out.println(o));
    }


    /**
     * Bool查询现在包括四种子句:must，filter,should,must_not
     *
     * GET /ecommerce/_search
     * {
     *   "query": {
     *     "bool": {
     *       "must": {
     *         "match":{
     *           "price":30
     *         }
     *       }
     *     }
     *   }
     * }
     **/
    @Test
    public void boolMust() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQuery()
                        .must(
                                matchQuery("price", 30)         //结构类似.bool -> must ->match
                        ))
                .build();


        elasticsearchTemplate.queryForList(searchQuery, Product.class)
                .forEach(o -> System.out.println(o));

    }



    @Test
    public void boolMultiMust(){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQuery()
                        .must(
                                rangeQuery("price").gte(25)
                        )
                        .mustNot(
                                matchQuery("name", "gaolujie")
                        ))
                .build();
    }


    @Test
    public void test() {
        //外层的过滤查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termsQuery("_id", "london"));

        Client client = elasticsearchTemplate.getClient();
        SearchRequestBuilder searchBuidler = client.prepareSearch()
                .setIndices("company")
                .setTypes("branch")
                .setFrom(0)
                .setSize(1)
                .setFetchSource(true)
                .setQuery(queryBuilder);

        log.debug("---> search request:{}", searchBuidler);
        SearchResponse response = searchBuidler.execute().actionGet();
        log.debug(response.toString());
    }
}