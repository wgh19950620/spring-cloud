package com.wgh.springcloud.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger2
 *
 * @author wangguanghui
 */
@Configuration
public class Swagger2 {

    /**
     * 通过 createRestApi函数来构建一个DocketBean
     * 函数名,可以随意命名,喜欢什么命名就什么命名
     */
    @Bean
    public Docket createRestApi() {
        /**
         * apiInfo 调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
         * apis    控制暴露出去的路径下的实例
         *         如果某个接口不想暴露,可以使用注解@ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
         */
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wgh.springcloud"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api 文档的详细信息函数
     *
     * @return ApiInfo实例
     */
    private ApiInfo apiInfo() {
        /**
         * title        页面标题
         * description  api描述
         * contact      创建人
         * version      版本号
         */
        return new ApiInfoBuilder()
                .title("spring cloud/redis 简单搭建")
                .description("简单优雅的restfun风格，http://172.20.15.32:8090/pages/viewpage.action?pageId=1213618")
                .termsOfServiceUrl("http://172.20.15.32:8090/pages/viewpage.action?pageId=1213618")
                .contact("wangguanghui")
                .version("1.0")
                .build();

    }
}
