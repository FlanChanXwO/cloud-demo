package cn.itcast.hotel;

import cn.itcast.hotel.pojo.HotelDoc;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;

@SpringBootTest
public class HotelRestClientTest {

    private RestHighLevelClient client;

    @BeforeEach
    public void init() {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.29.131:9200")));
    }

    @AfterEach
    public void destroy() throws IOException {
        client.close();
    }


    //通过RestClient查询所有文档数据
    @Test
    void selectAll() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //通过QueryBuilders组织DSL参数
        request.source().query(QueryBuilders.matchAllQuery());
        //发送请求，获取响应结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应结果
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
        });
    }


    //通过RestClient全文搜索文档数据 (分词搜索 - match)
    @Test
    void selectByCondition() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //通过QueryBuilders组织DSL参数
        request.source().query(QueryBuilders.matchQuery("name","上海"));
        //发送请求，获取响应结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应结果
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
        });
    }

    //通过RestClient全文搜索文档数据 (精确搜索 - term)
    @Test
    void selectByCertainValue() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //通过QueryBuilders组织DSL参数
        request.source().query(QueryBuilders.termQuery("name","上海"));
        //发送请求，获取响应结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应结果
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
        });
    }
    
    
    
    //复合查询
    @Test
    void complexSelect() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //通过BoolQueryBuilder组织DSL参数
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //组织多个查询条件以形成复合查询
        boolQuery.must(QueryBuilders.matchQuery("name","上海"));
        boolQuery.mustNot(QueryBuilders.matchQuery("brand","如家"));
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(1000).lte(3000));
        request.source().query(boolQuery);
        //发送请求，获取响应结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应结果
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
        });
    }


    //通过RestClient分页查询文档数据
    @Test
    void selectPage() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //组织DSL参数
        //当前页数
        int current = 40;
        //每页显示的文档数
        int size = 20 ;
        //通过QueryBuilders组织DSL参数
        request.source().query(QueryBuilders.matchAllQuery()).from(current).size(size);
        //发送请求，获取响应结果
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应结果
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(), HotelDoc.class));
        });
    }
}
