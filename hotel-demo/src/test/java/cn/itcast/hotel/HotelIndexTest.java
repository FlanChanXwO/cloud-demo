package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.IHotelService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class HotelIndexTest {

    private static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_smart\",\n" +
            "        \"index\": true,\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"address\": {\n" +
            "        \"type\": \"keyword\", \n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"price\": {\n" +
            "        \"type\": \"integer\", \n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"score\": {\n" +
            "        \"type\": \"integer\", \n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"brand\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true,\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"city\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true,\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"starName\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"business\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"geo_point\",\n" +
            "        \"index\": true\n" +
            "      },\n" +
            "      \"pic\":{\n" +
            "        \"type\": \"keyword\", \n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"index\": true\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    private RestHighLevelClient client;

    @Autowired
    private IHotelService hotelService;

    @BeforeEach
    void init() {
        //初始化客户端
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.29.131:9200")
        ));
    }

    @AfterEach
    void destroy() throws IOException {
        this.client.close();
    }


    //Es客户端初始化测试
    @Test
    void testInit() {
        System.out.println(client);
    }

    //Es客户端查询索引库
    @Test
    void selectHotelIndex() throws IOException {
        //创建request对象
        GetIndexRequest request = new GetIndexRequest("hotel");
        //发起请求，返回response对象
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        //处理返回的数据
        for (String index : response.getIndices()) {
            System.out.println(index);
        }
    }

    //Es客户端创建索引库
    @Test
    void createHotelIndex() throws IOException {
        //创建request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        //设置请求参数，设置DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        //发送请求 返回indices，其包含了操作索引库的所有方法
        client.indices().create(request, RequestOptions.DEFAULT);
    }


    //增加酒店数据到索引库
    @Test
    void addNewDocument1() throws IOException {
        //创建request对象
        IndexRequest request = new IndexRequest("hotel").id("1");
        //设置请求参数，设置DSL语句
        String jsonStr = "{\"name\":\"Jack\",\"age\":21}";
        request.source(jsonStr,XContentType.JSON);
        //发送请求 返回indexResponse对象
        client.index(request,RequestOptions.DEFAULT);
    }


    //增加文档
    @Test
    void addNewDocument2() throws IOException {
        Hotel hotel = hotelService.getById(61083);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        //创建request对象
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        //设置请求参数，设置DSL语句
        request.source(JSON.toJSONString(hotelDoc) , XContentType.JSON);
        //发送请求 返回indexResponse对象
        client.index(request,RequestOptions.DEFAULT);
    }


    //批量添加文档数据
    @Test
    void AddDocumentBatch() throws IOException {
        //创建request对象
        BulkRequest request = new BulkRequest();
        //把数据库中数据转化为文档添加到请求对象中
        hotelService.list().forEach((hotel -> {
            IndexRequest index = new IndexRequest("hotel").id(String.valueOf(hotel.getId()));
            index.source(JSON.toJSONString(new HotelDoc(hotel)),XContentType.JSON);
            request.add(index);
        }));
        //发送请求
        client.bulk(request,RequestOptions.DEFAULT);
    }


    //查询文档
    @Test
    void DocumentQuery1() throws IOException {
        //创建request对象
        GetRequest request = new GetRequest("hotel").id(String.valueOf(61083));
        //发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //解析响应的JSON字符串内容
        String jsonStr = response.getSourceAsString();
        //将JSON字符串转化为JAVA对象并输出
        HotelDoc hotelDoc = JSON.parseObject(jsonStr, HotelDoc.class);
        System.out.println(hotelDoc);
    }

    //查询文档
    @Test
    void DocumentQuery2() throws IOException {
        //创建request对象
        GetRequest request = new GetRequest("hotel","36934");
        //发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //解析响应的JSON字符串内容
        String jsonStr = response.getSourceAsString();
        //将JSON字符串转化为JAVA对象并输出
        HotelDoc hotelDoc = JSON.parseObject(jsonStr, HotelDoc.class);
        System.out.println(hotelDoc);
    }


    //查询所有文档
    @Test
    void DocumentQuery3() throws IOException {
        //创建request对象
        SearchRequest request = new SearchRequest("hotel");
        //发送请求,返回响应对象
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析响应的内容
        System.out.println();
        response.getHits().forEach((hit) -> {
            System.out.println(JSON.parseObject(hit.getSourceAsString(),HotelDoc.class));
        });
        System.out.println();
    }


    //删除文档
    @Test
    void DeleteDocument() throws IOException {
        //创建request对象
        DeleteRequest request = new DeleteRequest("hotel","1");
        //发送请求
        client.delete(request,RequestOptions.DEFAULT);
    }


    //更新文档
    @Test
    void UpdateDocument() throws IOException {
        //创建request对象
        UpdateRequest request = new UpdateRequest("hotel","36934");
        //设置更新内容，每2个为一对更新内容键值对
        request.doc("name","维也纳酒店（成都航空职业技术学院）");
        //发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

}
