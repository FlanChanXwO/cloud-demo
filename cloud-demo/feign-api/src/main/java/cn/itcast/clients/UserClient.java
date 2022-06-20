package cn.itcast.clients;

import cn.itcast.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 声明式Http远程调用：
 * 使用Feign客户端发起远程调用获取其它服务的数据
 * 增加@FeignClient("服务名")注解声明该接口为Feign客户端
 */
@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
