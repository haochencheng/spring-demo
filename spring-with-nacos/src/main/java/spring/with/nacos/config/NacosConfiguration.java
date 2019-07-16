package spring.with.nacos.config;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-13 21:56
 **/
@Configuration
@EnableNacosConfig
@NacosPropertySource(dataId = "spring-with-nacos.properties", autoRefreshed = true)
public class NacosConfiguration {

}
