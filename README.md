### spring cloud demo

1. 在启动类中实例化RestTemplate的时候加上@LoadBalanced注解时该服务器会开始负载均衡
使用RestTemplate调用其他服务器的接口时，url需要拼接spring.application.name
时命名的名称，如："http://spring-cloud-customer/hello/"
2. 在对实例化RestTemplate的时候分别开有无@LoadBalanced注解时，注入RestTemplate
使用的时候系统分不清使用哪个实例化后的RestTemplate,所以注入时需要加@Qualifier来指定
