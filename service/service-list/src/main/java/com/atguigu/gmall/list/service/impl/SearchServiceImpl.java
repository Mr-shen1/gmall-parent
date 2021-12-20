package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.service.SearchService;
import com.atguigu.gmall.model.list.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/19
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public SearchResponseVo search(SearchParam searchParam) {

        // 封装条件
        Query query = buildQuery(searchParam);
        // 查询后的返回值
        SearchHits<Goods> searchHits = elasticsearchRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        // 返回结果
        SearchResponseVo searchResponseVo = buildSearchResponseVo(searchHits, searchParam);
        return searchResponseVo;
    }

    private SearchResponseVo buildSearchResponseVo(SearchHits<Goods> hits, SearchParam searchParam) {
        log.info("buildSearchResponseVo() called with parameters => 【searchHits = {}】, 【searchParam = {}】", hits, searchParam);

        SearchResponseVo searchResponseVo = new SearchResponseVo();
        // 1 总记录数
        long totalHits = hits.getTotalHits();
        searchResponseVo.setTotal(totalHits);

        // 2 Goods信息
        List<Goods> goodsList = new ArrayList<>();
        List<SearchHit<Goods>> searchHits = hits.getSearchHits();
        for (SearchHit<Goods> searchHit : searchHits) {
            Goods goods = searchHit.getContent();
            if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                List<String> highlightField = searchHit.getHighlightField("title");
                goods.setTitle(highlightField.get(0));
            }
            goodsList.add(goods);
        }
        searchResponseVo.setGoodsList(goodsList);

        // 3 每页条数
        Integer pageSize = searchParam.getPageSize();
        searchResponseVo.setPageSize(pageSize);

        //4 当前页
        Integer pageNo = searchParam.getPageNo();
        searchResponseVo.setPageNo(pageNo);

        // 5 总条数
        searchResponseVo.setTotalPages(totalHits % pageSize == 0 ? totalHits / pageSize : (totalHits / pageSize) + 1);

        // 6 品牌信息
        List<SearchResponseTmVo> trademarkList = new ArrayList<>();

        Aggregations aggregations = hits.getAggregations();
        ParsedLongTerms tmAgg = aggregations.get("tm_agg");
        List<? extends Terms.Bucket> buckets = tmAgg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();

            // 添加tmId信息
            Number number = bucket.getKeyAsNumber();
            searchResponseTmVo.setTmId(number.longValue());

            // 添加tmLogo
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogo_agg");
            String tmLogoUrl = tmLogoAgg.getBuckets().get(0).getKeyAsString();
            searchResponseTmVo.setTmLogoUrl(tmLogoUrl);

            // 添加tmName
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmName_agg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            searchResponseTmVo.setTmName(tmName);

            trademarkList.add(searchResponseTmVo);
        }
        searchResponseVo.setTrademarkList(trademarkList);

        // 6 属性信息
        List<SearchResponseAttrVo> attrsList = new ArrayList<>();

        ParsedNested propsAgg = hits.getAggregations().get("props_agg");
        ParsedLongTerms attrIdAgg = propsAgg.getAggregations().get("attrId_agg");
        List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
        for (Terms.Bucket attrIdAggBucket : attrIdAggBuckets) {
            SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
            // 添加attrId
            long attrId = attrIdAggBucket.getKeyAsNumber().longValue();
            searchResponseAttrVo.setAttrId(attrId);
            // 添加attrName
            ParsedStringTerms attrNameAgg = attrIdAggBucket.getAggregations().get("attrName_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            searchResponseAttrVo.setAttrName(attrName);
            // 添加attrValueList
            ParsedStringTerms attrValueAgg = attrIdAggBucket.getAggregations().get("attrValue_agg");
            List<String> attrValueList = attrValueAgg.getBuckets().stream().map((e) -> e.getKeyAsString()).collect(Collectors.toList());
            searchResponseAttrVo.setAttrValueList(attrValueList);
            attrsList.add(searchResponseAttrVo);

        }
        searchResponseVo.setAttrsList(attrsList);

        return searchResponseVo;

    }

    // 构建DSL语句
    private Query buildQuery(SearchParam searchParam) {
        log.info("buildQuery() called with parameters => 【searchParam = {}】", searchParam);
        // 创建条件构造者
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 构建三级分类id查询条件
        Long category3Id = searchParam.getCategory3Id();
        if (category3Id != null) {
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category3Id", category3Id);

            // 条件构造
            boolQueryBuilder.must(termQueryBuilder);
        }

        // 构建品牌搜索条件
        // trademark=   1:小米
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split != null && split.length > 0) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tmId", split[0]);
                boolQueryBuilder.must(termQueryBuilder);
            }
        }

        // 搜索关键字搜索
        // keyword=小米
        String keyword = searchParam.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            //String[] split = keyword.split(":");
            //if (split != null && split.length > 0) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
            //}
        }

        // 属性构建
        // props=23:8G:运行内存
        // props=24:256G:机身内存
        String[] props = searchParam.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                String[] split = prop.split(":");
                if (split != null && split.length > 0) {
                    BoolQueryBuilder boolQueryBuilder2Props = QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery("attrs.attrId", split[0]))
                            .must(QueryBuilders.termQuery("attrs.attrValue", split[1]));

                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", boolQueryBuilder2Props, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                }
            }
        }

        // 放入构建器中
        builder.withQuery(boolQueryBuilder);

        // 构建分页信息
        Integer pageNo = searchParam.getPageNo();
        Integer pageSize = searchParam.getPageSize();

        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        builder.withPageable(pageRequest);

        // 构建排序信息
        // order=1:desc
        String order = searchParam.getOrder();

        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            if (split.length > 0 && split != null) {

                FieldSortBuilder fieldSortBuilder = SortBuilders
                        .fieldSort(split[0].equals("1") ? "hotScore" : "price")
                        .order(split[1].equals("asc") ? SortOrder.ASC : SortOrder.DESC);

                builder.withSort(fieldSortBuilder);
            }
        }

        // 高亮
        if (!StringUtils.isEmpty(keyword)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title")
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            builder.withHighlightBuilder(highlightBuilder);
        }

        // ###############聚合###############
        // 品牌相关信息聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("tm_agg")
                .field("tmId").size(1000)
                .subAggregation(
                        AggregationBuilders.terms("tmName_agg").field("tmName").size(1000)
                )
                .subAggregation(
                        AggregationBuilders.terms("tmLogo_agg").field("tmLogoUrl").size(1000)
                );

        builder.addAggregation(termsAggregationBuilder);

        // 平台属性相关聚合
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders
                .nested("props_agg", "attrs")
                .subAggregation(
                        AggregationBuilders.terms("attrId_agg").field("attrs.attrId").size(1000)
                                .subAggregation(
                                        AggregationBuilders.terms("attrName_agg").field("attrs.attrName").size(1000)
                                )
                                .subAggregation(
                                        AggregationBuilders.terms("attrValue_agg").field("attrs.attrValue").size(1000)
                                )
                );
        builder.addAggregation(nestedAggregationBuilder);

        NativeSearchQuery build = builder.build();
        log.info("buildQuery() returned: " + build);

        return build;


    }

}
