package org.flanzone;

import org.apache.shardingsphere.readwritesplitting.algorithm.StaticReadwriteSplittingType;
import org.apache.shardingsphere.readwritesplitting.algorithm.loadbalance.RoundRobinReplicaLoadBalanceAlgorithm;
import org.apache.shardingsphere.readwritesplitting.spi.ReadwriteSplittingType;
import org.apache.shardingsphere.readwritesplitting.spi.ReplicaLoadBalanceAlgorithm;
import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan(basePackages = "org.flanzone.mapper")
@SpringBootApplication
public class ShardingJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingJdbcApplication.class, args);
	}

	/**
	 * 配置主键生成算法 - 雪花算法
	 */
	@Bean
	public KeyGenerateAlgorithm snowflake() {
		return new SnowflakeKeyGenerateAlgorithm();
	}

	/**
	 * 负载均衡算法 - 轮询算法
	 */
	@Bean
	public ReplicaLoadBalanceAlgorithm round_robin() {
		return new RoundRobinReplicaLoadBalanceAlgorithm();
	}

}
