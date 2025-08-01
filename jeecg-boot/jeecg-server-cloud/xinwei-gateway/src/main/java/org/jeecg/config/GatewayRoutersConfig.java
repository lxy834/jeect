package org.jeecg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author scott
 * @date 2020/05/26
 * 路由配置信息
 */
@Configuration
@RefreshScope
public class GatewayRoutersConfig {
    /**
     * 路由配置方式：database，yml，nacos
     */
    public String dataType;
    public String serverAddr;
    public String namespace;
    public String dataId;
    public String routeGroup;
    public String username;
    public String password;

    @Value("${jeecg.route.config.data-type:#{null}}")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Value("${spring.cloud.consul.host}")
    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getDataType() {
        return dataType;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getDataId() {
        return dataId;
    }

    public String getRouteGroup() {
        return routeGroup;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
