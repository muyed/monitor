package com.muye.monitor.component;

import com.muye.monitor.DO.MonitorMethodDO;
import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.util.DateUtil;
import com.muye.monitor.common.util.EnvUtil;
import com.muye.monitor.dao.MonitorMethodDAO;
import com.muye.monitor.dto.MethodReportDTO;
import com.muye.monitor.dto.ProductReportDTO;
import com.muye.monitor.query.*;
import com.muye.monitor.common.util.JsonUtil;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EsComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsComponent.class);

    private static final String ES_INDEX = "monitor";

    private static final String ES_TYPE = "result";

    private static final String LOCAL_ES_HOST = "47.97.181.47";
    private static final String DAILY_ES_HOST = "192.168.30.98";

    private static final Integer ES_PORT = 9222;

    private static final String ES_USERNAME = "elastic";

    private static final String ES_PASSWORD = "Songshu#619";

    private RestHighLevelClient client;

    @Resource
    private MonitorMethodDAO monitorMethodDAO;

    @PostConstruct
    public void init(){
        RestClientBuilder builder = RestClient.builder(new HttpHost(EnvUtil.isDaily() ? DAILY_ES_HOST : LOCAL_ES_HOST, ES_PORT))
                .setHttpClientConfigCallback(b -> {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(ES_USERNAME, ES_PASSWORD));
                    b.setMaxConnTotal(100);
                    b.setMaxConnPerRoute(100);
                    return b.setDefaultCredentialsProvider(credentialsProvider);
                })
                .setRequestConfigCallback(b -> {
                    b.setConnectTimeout(3000);
                    b.setSocketTimeout(30000);
                    b.setConnectionRequestTimeout(3000);
                    return b;
                });

        client = new RestHighLevelClient(builder);


    }

    public void push(List<MonitorResult> dataList, boolean refresh) throws Exception{
        BulkRequest request = new BulkRequest();
        for (MonitorResult data : dataList) {
            IndexRequest indexRequest = new IndexRequest(ES_INDEX, ES_TYPE, data.getId().toString()).source(new BeanMap(data));
            request.add(indexRequest);
        }

        if (refresh) {
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }

        client.bulkAsync(request, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                LOGGER.debug("推送数据成功: {}", JsonUtil.toJson(bulkItemResponses));
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("推送数据失败", e);
            }
        });

    }

    public void delete(List<Long> ids){
        BulkRequest request = new BulkRequest();
        for (Long id : ids) {
            DeleteRequest deleteRequest = new DeleteRequest(ES_INDEX, ES_TYPE, id.toString());
            request.add(deleteRequest);
        }
        client.bulkAsync(request, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                LOGGER.debug("清除数据成功: {}", JsonUtil.toJson(bulkItemResponses));
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("清除数据失败", e);
            }
        });
    }

    public List<MonitorResult> query(MonitorQuery query){

        SearchRequest request = new SearchRequest(ES_INDEX).types(ES_TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        searchSourceBuilder.from(query.getCurrentPage() == 1 ? 0 : (query.getCurrentPage() - 1) * query.getPageSize() - 1);
        searchSourceBuilder.size(query.getPageSize());

        buildQuery(query, boolQueryBuilder, searchSourceBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        try {
            SearchResponse response = client.search(request);
            if (response.status() != RestStatus.OK || response.getHits().getHits().length <= 0) {
                return new ArrayList<>();
            }
            query.setTotalRecored((int)response.getHits().totalHits);
            return Stream.of(response.getHits().getHits()).map(b -> JsonUtil.fromJson(b.getSourceAsString(), MonitorResult.class)).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("查询es失败", e);
        }
        return new ArrayList<>();
    }

    public List<MonitorResult> queryByScrollId(MonitorScrollQuery scrollQuery){
        if (StringUtils.isEmpty(scrollQuery.getScrollId())) {
            SearchRequest request = new SearchRequest(ES_INDEX).types(ES_TYPE);
            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5));
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(scrollQuery.getQuery().getPageSize());

            buildQuery(scrollQuery.getQuery(), boolQueryBuilder, searchSourceBuilder);

            searchSourceBuilder.query(boolQueryBuilder);
            request.source(searchSourceBuilder);
            request.scroll(scroll);
            try {
                SearchResponse response = client.search(request);
                if (response.status() != RestStatus.OK || response.getHits().getHits().length <= 0) {
                    return new ArrayList<>();
                }
                scrollQuery.setScroll(scroll);
                scrollQuery.setScrollId(response.getScrollId());
                return Stream.of(response.getHits().getHits()).map(b -> JsonUtil.fromJson(b.getSourceAsString(), MonitorResult.class)).collect(Collectors.toList());
            } catch (Exception e) {
                LOGGER.error("查询es失败", e);
            }
        } else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollQuery.getScrollId());
            scrollRequest.scroll(scrollQuery.getScroll());
            try {
                SearchResponse response = client.searchScroll(scrollRequest);
                scrollQuery.setScrollId(response.getScrollId());
                if (response.status() != RestStatus.OK || response.getHits().getHits().length == 0) {
                    creanScroll(scrollQuery.getScrollId());
                    return new ArrayList<>();
                }
                return Stream.of(response.getHits().getHits()).map(b -> JsonUtil.fromJson(b.getSourceAsString(), MonitorResult.class)).collect(Collectors.toList());
            } catch (Exception e) {
                LOGGER.error("查询es失败", e);
            }
        }
        return new ArrayList<>();
    }

    public List<ProductReportDTO> getProductReport(ProductReportQuery query){
        List<ProductReportDTO> list = new ArrayList<>();
        if (StringUtils.isEmpty(query.getStartTime())) {
            query.setStartTime(DateUtil.format(DateUtil.addDate(new Date(), Calendar.MINUTE, -2), "yyyy-MM-dd HH:mm:ss"));
        }
        if (StringUtils.isEmpty(query.getEndTime())) {
            query.setEndTime(DateUtil.format(DateUtil.addDate(new Date(), Calendar.MINUTE, -1), "yyyy-MM-dd HH:mm:ss"));
        }

        SearchRequest request = new SearchRequest(ES_INDEX).types(ES_TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group").field("productName")
                .subAggregation(AggregationBuilders.count("count").field("speed"))
                .subAggregation(AggregationBuilders.avg("avg").field("speed"))
                .subAggregation(AggregationBuilders.max("max").field("speed"))
                .subAggregation(AggregationBuilders.terms("slowMethodGroup").field("fullMethodName")
                        .subAggregation(AggregationBuilders.avg("avg").field("speed"))
                        .order(BucketOrder.aggregation("avg", false))
                        .size(50))
                .subAggregation(AggregationBuilders.terms("hotMethodGroup").field("fullMethodName")
                        .subAggregation(AggregationBuilders.count("count").field("speed"))
                        .order(BucketOrder.aggregation("count", false))
                        .size(50))
                .subAggregation(AggregationBuilders.terms("exceptionMethodGroup").field("fullMethodName")
                        .subAggregation(AggregationBuilders.sum("sum").field("status"))
                        .order(BucketOrder.aggregation("sum", false))
                        .size(50));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.size(0);
        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        termsAggregationBuilder.size(100);

        boolQueryBuilder.filter(QueryBuilders.termQuery("trackLevel", "1"));
        if (!StringUtils.isEmpty(query.getEnv())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("env", query.getEnv()));
        }
        if (!StringUtils.isEmpty(query.getProductName())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("productName", query.getProductName()));
        }
        try {
            RangeQueryBuilder startTimeQuery = QueryBuilders.rangeQuery("startTime").gte(DateUtil.toDate(query.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            if (!StringUtils.isEmpty(query.getEndTime())) {
                startTimeQuery.lte(DateUtil.toDate(query.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            boolQueryBuilder.must(startTimeQuery);
        } catch (ParseException e) {
        }

        try {
            SearchResponse response = client.search(request);
            Terms terms = response.getAggregations().get("group");

            List<MonitorMethodDO> methodDOS = monitorMethodDAO.query(query.getEnv(), query.getProductName(), null);

            if (terms.getBuckets().size() == 0 && query.getEnv() == "prod") {
                LOGGER.error(response.toString());
            }

            for (Terms.Bucket b : terms.getBuckets()) {
                ProductReportDTO report = new ProductReportDTO();
                report.setEnv(query.getEnv());
                report.setStartTime(query.getStartTime());
                report.setEndTime(query.getEndTime());
                report.setProductName(b.getKeyAsString());
                report.setMaxResponseTime((int)((ParsedMax)b.getAggregations().get("max")).getValue());
                report.setRequestCount(((ParsedValueCount)b.getAggregations().get("count")).getValue());
                report.setAveResponseTime((int)((Avg)b.getAggregations().get("avg")).getValue());

                Terms slowTerms = b.getAggregations().get("slowMethodGroup");
                Terms hotTerms = b.getAggregations().get("hotMethodGroup");
                Terms exceptionTerms = b.getAggregations().get("exceptionMethodGroup");

                List<Pair<String, Long>> topSlowMethod = new ArrayList<>();
                List<Pair<String, Long>> topHotMethod = new ArrayList<>();
                List<Pair<String, Long>> topUnknownExpMethod = new ArrayList<>();
                List<Map<String, Object>> timeOutMethods = new ArrayList<>();

                for (Terms.Bucket sb : slowTerms.getBuckets()) {
                    Pair<String, Long> slow = new ImmutablePair<>(sb.getKeyAsString(), (long)((Avg)sb.getAggregations().get("avg")).getValue());
                    topSlowMethod.add(slow);

                    Integer threshold = methodDOS.stream()
                            .filter(d -> d.getFullMethodName().equals(slow.getKey()))
                            .map(d -> d.getTimeOut())
                            .findFirst()
                            .orElse(500);

                    if (slow.getValue().intValue() > threshold) {
                        Map<String, Object> timeOut = new HashMap<>();
                        timeOut.put("fullMethodName", slow.getKey());
                        timeOut.put("avgResponseTime", slow.getValue());
                        timeOut.put("threshold", threshold);
                        timeOutMethods.add(timeOut);
                    }
                }

                for (Terms.Bucket hb : hotTerms.getBuckets()) {
                    Pair<String, Long> hot = new ImmutablePair<>(hb.getKeyAsString(), ((ParsedValueCount)(hb.getAggregations().get("count"))).getValue());
                    topHotMethod.add(hot);
                }

                for (Terms.Bucket eb : exceptionTerms.getBuckets()) {
                    Pair<String, Long> excep = new ImmutablePair<>(eb.getKeyAsString(), (long)((ParsedSum)(eb.getAggregations().get("sum"))).getValue());
                    topUnknownExpMethod.add(excep);
                }

                if (topSlowMethod.size() > 50) {
                    topSlowMethod = topSlowMethod.subList(0, 50);
                }
                if (topHotMethod.size() > 50) {
                    topHotMethod = topHotMethod.subList(0, 50);
                }
                if (topUnknownExpMethod.size() > 50) {
                    topUnknownExpMethod = topUnknownExpMethod.subList(0, 50);
                }

                report.setTopSlowMethod(topSlowMethod);
                report.setTopHotMethod(topHotMethod);
                report.setTopUnknownExpMethod(topUnknownExpMethod);
                report.setTimeOutMethods(timeOutMethods);
                list.add(report);
            }
        } catch (Exception e) {
            LOGGER.error("查询es失败", e);
        }
        return list;
    }

    public List<MethodReportDTO> getMethodReport(MethodReportQuery query) {
        List<MethodReportDTO> list = new ArrayList<>();
        if (StringUtils.isEmpty(query.getStartTime())) {
            query.setStartTime(DateUtil.format(DateUtil.addDate(new Date(), Calendar.MINUTE, -5), "yyyy-MM-dd HH:mm:ss"));
        }

        SearchRequest request = new SearchRequest(ES_INDEX).types(ES_TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group").field("fullMethodName")
                .subAggregation(AggregationBuilders.count("count").field("speed"))
                .subAggregation(AggregationBuilders.avg("avg").field("speed"))
                .subAggregation(AggregationBuilders.max("max").field("speed"))
                .subAggregation(AggregationBuilders.sum("sum").field("status"));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(query.getEnv())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("env", query.getEnv()));
        }

        try {
            RangeQueryBuilder startTimeQuery = QueryBuilders.rangeQuery("startTime").gte(DateUtil.toDate(query.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            boolQueryBuilder.must(startTimeQuery);
            if (!StringUtils.isEmpty(query.getEndTime())) {
                startTimeQuery.lte(DateUtil.toDate(query.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            }
        } catch (ParseException e) {
        }
        if (!StringUtils.isEmpty(query.getEnv())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("env", query.getEnv()));
        }
        if (!StringUtils.isEmpty(query.getProductName())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("productName", query.getProductName()));
        }
        if (!StringUtils.isEmpty(query.getFullMethodName())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("fullMethodName", query.getFullMethodName()));
        }

        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.size(0);
        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        termsAggregationBuilder.size(1000);
        try {
            SearchResponse response = client.search(request);
            Terms terms = response.getAggregations().get("group");
            List<MonitorMethodDO> methodDOS = monitorMethodDAO.query(query.getEnv(), query.getProductName(), query.getFullMethodName());
            for (Terms.Bucket b : terms.getBuckets()) {
                MethodReportDTO methodReportDTO = new MethodReportDTO();
                methodReportDTO.setEnv(query.getEnv());
                methodReportDTO.setProductName(query.getProductName());
                methodReportDTO.setStartTime(query.getStartTime());
                methodReportDTO.setEndTime(query.getEndTime());
                methodReportDTO.setFullMethodName(b.getKeyAsString());
                methodReportDTO.setMaxResponseTime((long)((ParsedMax)b.getAggregations().get("max")).getValue());
                methodReportDTO.setRequestCount(((ParsedValueCount)b.getAggregations().get("count")).getValue());
                methodReportDTO.setAvgResponseTime((long)((Avg)b.getAggregations().get("avg")).getValue());
                methodReportDTO.setErrCount((long)((ParsedSum)b.getAggregations().get("sum")).getValue());
                methodReportDTO.setSuccessRate((methodReportDTO.getRequestCount() - methodReportDTO.getErrCount()) / Double.valueOf(methodReportDTO.getRequestCount()));

                for (MonitorMethodDO methodDO : methodDOS) {
                    if (methodDO.getFullMethodName().equals(methodReportDTO.getFullMethodName())) {
                        methodReportDTO.setTimeOut(methodReportDTO.getAvgResponseTime() > methodDO.getTimeOut());
                        break;
                    }
                }

                if(methodReportDTO.getTimeOut() == null) {
                    methodReportDTO.setTimeOut(methodReportDTO.getAvgResponseTime() > 500);
                }

                list.add(methodReportDTO);
            }
        } catch (Exception e) {
            LOGGER.error("查询es失败", e);
        }

        return list;
    }

    private void buildQuery(MonitorQuery query, BoolQueryBuilder boolQueryBuilder, SearchSourceBuilder searchSourceBuilder){
        //查询条件
        Field[] fields = query.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String k = field.getName();

            if (k.equals("start") || k.equals("end") || k.equals("startSpeed") || k.equals("endSpeed") || k.equals("sortColumn")) {
                continue;
            }

            try {
                Object v = field.get(query);
                if (v == null) {
                    continue;
                }
                boolQueryBuilder.filter(QueryBuilders.termQuery(k, v.toString()));

            } catch (Exception e) {
            }
        }

        //监控时间范围查询
        try {
            if (!StringUtils.isEmpty(query.getStart()) || StringUtils.isEmpty(query.getEnd())) {
                RangeQueryBuilder startTimeQuery = QueryBuilders.rangeQuery("startTime");
                if (!StringUtils.isEmpty(query.getStart())) {
                    startTimeQuery.gt(DateUtil.toDate(query.getStart(), "yyyy-MM-dd HH:mm:ss"));
                }
                if (!StringUtils.isEmpty(query.getEnd())) {
                    startTimeQuery.lte(DateUtil.toDate(query.getEnd(), "yyyy-MM-dd HH:mm:ss"));
                }
                boolQueryBuilder.filter(startTimeQuery);
            }
        }catch (ParseException e) {
        }

        //响应时间范围查询
        if (query.getStartSpeed() != null || query.getEndSpeed() != null) {
            RangeQueryBuilder speedQuery = QueryBuilders.rangeQuery("speed");
            if (query.getStartSpeed() != null) {
                speedQuery.gte(query.getStartSpeed());
            }
            if (query.getEndSpeed() != null) {
                speedQuery.lte(query.getEndSpeed());
            }
            boolQueryBuilder.filter(speedQuery);
        }

        //排序
        if (!StringUtils.isEmpty(query.getSortColumn())) {
            searchSourceBuilder.sort(new FieldSortBuilder(query.getSortColumn()).order(StringUtils.isEmpty(query.getSort()) || query.getSort().equals(BaseQuery.ASC) ? SortOrder.ASC : SortOrder.DESC));
        }
    }

    private void creanScroll(String scrollId){
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        try {
            client.clearScroll(clearScrollRequest);
        } catch (IOException e) {
        }
    }
}
