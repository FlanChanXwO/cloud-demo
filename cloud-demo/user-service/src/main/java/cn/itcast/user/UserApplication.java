package cn.itcast.user;

import cn.itcast.user.config.PatternProperties;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;

@MapperScan("cn.itcast.user.mapper")
@EnableConfigurationProperties({PatternProperties.class})
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /**
     * 设定负载均衡策略
     * 策略：轮询
     */
    @Bean
    public IRule roundRobinRule() {
        return new RoundRobinRule();
    }
}
