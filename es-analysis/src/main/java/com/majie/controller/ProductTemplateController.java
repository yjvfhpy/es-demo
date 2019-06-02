package com.majie.controller;

import com.majie.entity.Product;
import com.majie.enums.AnalyzerEnum;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * 复杂的操作使用 elasticsearchTemplate
 * 查询具体组合可以查看 # QueryBuilders 类
 *
 * @author majie
 * @date 2018/7/2.
 */
@RestController
@RequestMapping("/temp/product")
public class ProductTemplateController {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ProductTemplateController(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @GetMapping
    public Object findAll(@RequestParam String name, @RequestParam Integer code) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        matchQuery("name", name)
                                .analyzer(AnalyzerEnum.getNameByCode(code))   //mapping没有设置的时候这里可以设置分词类型
                )
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Product.class);
    }


}
