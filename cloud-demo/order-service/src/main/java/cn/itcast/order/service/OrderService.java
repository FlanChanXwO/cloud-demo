package cn.itcast.order.service;

import cn.itcast.clients.UserClient;
import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import cn.itcast.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.使用Feign实现远程调用来获取微服务提供的user数据
        User user = userClient.findById(orderId);
        // 3.封装user对象到order对象中
        order.setUser(user);
        // 4.返回
        return order;
    }



/*
    @Autowired
    private RestTemplate restTemplate;

    使用RestTemplate发起远程调用获取其它服务的数据
    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.利用restTemplate发送http请求查询用户
        // 2.1 url路径
        String url = "http://userservice/user/" + order.getUserId();
        // 2.2 发送http请求，实现远程调用
        User user = restTemplate.getForObject(url, User.class);
        // 3.封装user对象到order对象中
        order.setUser(user);
        // 4.返回
        return order;
    }
*/
}
