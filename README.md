### spring cloud demo

1. 在启动类中实例化RestTemplate的时候加上@LoadBalanced注解时该服务器会开始负载均衡
使用RestTemplate调用其他服务器的接口时，url需要拼接spring.application.name
时命名的名称，如："http://spring-cloud-customer/hello/"
2. 在对实例化RestTemplate的时候分别开有无@LoadBalanced注解时，注入RestTemplate
使用的时候系统分不清使用哪个实例化后的RestTemplate,所以注入时需要加@Qualifier来指定


- spring-cloud [springcloud_测试](https://github.com/wgh19950620/spring-cloud.git) 
- commons [springcloud_commons](https://github.com/wgh19950620/spring-cloud/tree/master/commons) 
- spring-cloud-server [spring-cloud-server](https://github.com/wgh19950620/spring-cloud/tree/master/springcloud-server) 
- spring-cloud-client [spring-cloud-client](https://github.com/wgh19950620/spring-cloud/tree/master/springcloud-client) 
- spring-cloud-customer [spring-cloud-customer](https://github.com/wgh19950620/spring-cloud/tree/master/springcloud-customer) 
