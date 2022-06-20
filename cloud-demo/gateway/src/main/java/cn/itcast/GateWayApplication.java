package cn.itcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 导入与gateway相关的依赖并进行网关相关配置即可启用网关
 *
 * GateWay网关的作用：
 * 1.权限控制
 * 2.路由请求到微服务做负载均衡
 */
@SpringBootApplication
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }
}
