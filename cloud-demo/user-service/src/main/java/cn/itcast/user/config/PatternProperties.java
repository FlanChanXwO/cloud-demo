package cn.itcast.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * resources目录下bootstrap.yml可以确保该配置文件优先被SpringBoot加载
 * 拉取nacos配置中心的配置信息的配置类
 * 使用配置类时不需要搭配@RefreshScope来及时获取配置中心的配置信息，会自动同步配置中心的配置信息
 * @author FlanChan
 */
@Data
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    private String dateformat;
    private String envSharedValue;
}
